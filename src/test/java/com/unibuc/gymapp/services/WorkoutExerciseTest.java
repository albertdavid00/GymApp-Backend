package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.SetDto;
import com.unibuc.gymapp.models.*;
import com.unibuc.gymapp.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseTest {
    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;
    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private SetRepository setRepository;
    private Workout workout;
    private User user;
    private Exercise exercise;
    private WorkoutExercise workoutExercise;
    private Set set;
    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L).build();
        workout = Workout.builder()
                .id(2L)
                .volume(100.0)
                .ended(false)
                .user(user)
                .build();
        exercise = Exercise.builder()
                .id(3L)
                .build();
        workoutExercise = WorkoutExercise.builder()
                .id(4L)
                .workout(workout)
                .exercise(exercise)
                .build();
        set = Set.builder()
                .id(5L)
                .weight(10.0)
                .repetitions(12)
                .setType(SetType.NORMAL)
                .build();
    }

    @Test
    @DisplayName("Update workout volume - expected success")
    public void updateWorkoutVolume() {
        Double weight = 10.0;
        Integer reps = 10;
        Workout updateWorkout = Workout.builder()
                .volume(200.0)
                .build();

        workoutExerciseService.updateWorkoutVolume(workout, weight, reps);

        verify(workoutRepository).save(any(Workout.class));
        Assertions.assertEquals(updateWorkout.getVolume(), workout.getVolume());
    }

    @Test
    @DisplayName("Add exercise to workout - expected success")
    public void addExerciseToWorkout() {
        when(workoutRepository.findActiveWorkoutOfUser(workout.getId(), user.getId())).thenReturn(Optional.of(workout));
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(workoutExerciseRepository.findByWorkoutAndExercise(workout, exercise)).thenReturn(Optional.empty());
        when(workoutExerciseRepository.save(any(WorkoutExercise.class))).thenReturn(workoutExercise);

        Long result = workoutExerciseService.addExerciseToWorkout(workout.getId(), exercise.getId(), user.getId());

        verify(workoutExerciseRepository).save(any(WorkoutExercise.class));
        Assertions.assertEquals(workoutExercise.getId(), result);
    }

    @Test
    @DisplayName("Add exercise - expected Active Workout Not Found")
    public void addExerciseActiveWorkoutNotFound() {
        workout.setEnded(true);
        String message = "Active workout with id " + workout.getId() + " not found!";
        when(workoutRepository.findActiveWorkoutOfUser(workout.getId(), user.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            workoutExerciseService.addExerciseToWorkout(workout.getId(), exercise.getId(), user.getId());
        });
        Assertions.assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Add exercise - expected Exercise Not Found")
    public void addExerciseNotFound() {
        String message = "Exercise with id " + exercise.getId() + " not found!";


        when(workoutRepository.findActiveWorkoutOfUser(workout.getId(), user.getId())).thenReturn(Optional.of(workout));
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            workoutExerciseService.addExerciseToWorkout(workout.getId(), exercise.getId(), user.getId());
        });
        Assertions.assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Add exercise - expected Bad Request")
    public void addExerciseAlreadyExists() {
        String message = "Exercise already in workout!";

        when(workoutRepository.findActiveWorkoutOfUser(workout.getId(), user.getId())).thenReturn(Optional.of(workout));
        when(exerciseRepository.findById(exercise.getId())).thenReturn(Optional.of(exercise));
        when(workoutExerciseRepository.findByWorkoutAndExercise(workout, exercise)).thenThrow(new BadRequestException(message));

        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            workoutExerciseService.addExerciseToWorkout(workout.getId(), exercise.getId(), user.getId());
        });
        Assertions.assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Add set to exercise - expected success")
    public void addSetToExercise() {
        SetDto setDto = SetDto.builder()
                .weight(10.0)
                .repetitions(12)
                .setType(SetType.NORMAL)
                .build();
        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        when(setRepository.save(any(Set.class))).thenReturn(set);

        Long result = workoutExerciseService.addSetToWorkoutExercise(setDto, workoutExercise.getId(), user.getId());
        verify(setRepository).save(any(Set.class));
        Assertions.assertEquals(set.getId(), result);
    }

    @Test
    @DisplayName("Add set to execise - expected Not Found")
    public void addSetToExerciseNotFound() {
        SetDto setDto = SetDto.builder()
                .weight(10.0)
                .repetitions(12)
                .setType(SetType.NORMAL)
                .build();
        String message = "Workout exercise with id " + workoutExercise.getId() + " not found!";

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            workoutExerciseService.addSetToWorkoutExercise(setDto, workoutExercise.getId(), user.getId());
        });
        Assertions.assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Add set to exercise - expect Invalid Workout")
    public void addSetToExerciseInvalid() {
        SetDto setDto = SetDto.builder()
                .weight(10.0)
                .repetitions(12)
                .setType(SetType.NORMAL)
                .build();
        workout.setEnded(true);

        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            workoutExerciseService.addSetToWorkoutExercise(setDto, workoutExercise.getId(), user.getId());
        });
        Assertions.assertEquals("Invalid workout!", thrown.getMessage());
    }

    @Test
    @DisplayName("Delete exercise from workout - expect success")
    public void deleteExerciseFromWorkout() {
        when(workoutExerciseRepository.findById(workoutExercise.getId())).thenReturn(Optional.of(workoutExercise));
        workoutExerciseService.deleteWorkoutExercise(workoutExercise.getId(), user.getId());
        verify(workoutExerciseRepository).delete(any(WorkoutExercise.class));
    }
}
