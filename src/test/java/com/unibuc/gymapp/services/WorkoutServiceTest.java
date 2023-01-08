package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.NewWorkoutDto;
import com.unibuc.gymapp.models.Gym;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.repositories.GymRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import com.unibuc.gymapp.repositories.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {
    @InjectMocks
    private WorkoutService workoutService;
    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GymRepository gymRepository;
    private User user;
    private Workout workout;
    private Gym gym;
    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .build();
        gym = Gym.builder()
                .id(2L)
                .name("Gym Test")
                .build();
        workout = Workout.builder()
                .id(3L)
                .ended(false)
                .creationTime(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .volume(0.0)
                .user(user)
                .gym(gym)
                .build();
    }

    @Test
    @DisplayName("Add Workout - expect success")
    public void addWorkout() {
        NewWorkoutDto newWorkoutDto = NewWorkoutDto.builder()
                .title("Test")
                .gymId(2L)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(gymRepository.findById(gym.getId())).thenReturn(Optional.of(gym));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        Long result = workoutService.addWorkout(newWorkoutDto, user.getId());
        verify(workoutRepository).save(any(Workout.class));
        assertEquals(workout.getId(), result);
    }

    @Test
    @DisplayName("Add Workout - expect User Not Found")
    public void addWorkoutUserNotFound() {
        NewWorkoutDto newWorkoutDto = NewWorkoutDto.builder()
                .title("Test")
                .gymId(2L)
                .build();
        String message = "User with id " + user.getId() + " doesn't exist!";
        when(userRepository.findById(user.getId())).thenThrow(new NotFoundException(message));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            workoutService.addWorkout(newWorkoutDto, user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Add workout - expect Gym Not Found")
    public void addWorkoutGymNotFound() {
        NewWorkoutDto newWorkoutDto = NewWorkoutDto.builder()
                .title("Test")
                .gymId(2L)
                .build();
        String message = "Gym with id " + newWorkoutDto.getGymId() + " not found!";
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(gymRepository.findById(gym.getId())).thenThrow(new NotFoundException(message));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            workoutService.addWorkout(newWorkoutDto, user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Finish workout - expect success")
    public void finishWorkout() {
        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

        workoutService.finishWorkout(workout.getId(), user.getId());
        verify(workoutRepository).save(any(Workout.class));
    }

    @Test
    @DisplayName("Finish workout - expect Not Found")
    public void finishWorkoutNotFound() {
        String message = "Workout with id " + workout.getId() + "not found!";

        when(workoutRepository.findById(workout.getId())).thenThrow(new NotFoundException(message));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            workoutService.finishWorkout(workout.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Finish workout - expect Bad Request Belonging")
    public void finishWorkoutBadRequestBelonging() {
        String message = "You cannot update workouts that are not yours!";

        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.ofNullable(workout));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            workoutService.finishWorkout(workout.getId(), user.getId() + 1);
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Finish workout - expect Bad Request Workout Ended")
    public void finishWorkoutAlreadyEnded() {
        String message = "Workout with id " + workout.getId() + " already ended!";
        workout.setEnded(true);
        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.ofNullable(workout));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            workoutService.finishWorkout(workout.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

}
