package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.SetDto;
import com.unibuc.gymapp.models.*;
import com.unibuc.gymapp.repositories.SetRepository;
import com.unibuc.gymapp.repositories.WorkoutExerciseRepository;
import org.aspectj.weaver.ast.Not;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SetServiceTest {
    @InjectMocks
    private SetService setService;
    @Mock
    private SetRepository setRepository;
    @Mock
    private WorkoutExerciseService workoutExerciseService;
    private User user;
    private Workout workout;
    private WorkoutExercise workoutExercise;
    private Set set;

    @BeforeEach
    public void setup() {
         user = User.builder()
                .id(1L).build();
         workout = Workout.builder()
                .id(2L)
                 .volume(100.0)
                 .user(user)
                 .build();
         workoutExercise = WorkoutExercise.builder()
                .id(3L)
                .workout(workout)
                 .build();
         set = Set.builder()
                .id(4L)
                 .weight(10.0)
                 .repetitions(3)
                 .setType(SetType.NORMAL)
                .workoutExercise(workoutExercise)
                .build();
    }

    @Test
    @DisplayName("Delete Set - expect success")
    public void deleteSet() {
        when(setRepository.findById(set.getId())).thenReturn(Optional.of(set));

        setService.deleteSet(set.getId(), user.getId());
        verify(workoutExerciseService).updateWorkoutVolume(workout, -set.getWeight(), set.getRepetitions());
        verify(setRepository).delete(any(Set.class));
    }

    @Test
    @DisplayName("Delete Set - expect Not Found Exception")
    public void deleteSetNotFound() {
        String message = "Set with id " + set.getId() + " not found";

        when(setRepository.findById(set.getId())).thenThrow(new NotFoundException(message));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            setService.deleteSet(set.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Delete Set - expect Bad Request Exception")
    public void deleteSetBadRequest() {
        String message = "You can only delete your sets!";
        when(setRepository.findById(set.getId())).thenReturn(Optional.of(set));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            setService.deleteSet(set.getId(), user.getId() + 1);
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Update Set - expect success")
    public void updateSet() {
        SetDto setDto = SetDto.builder()
                .weight(12.0)
                .repetitions(11)
                .setType(SetType.NORMAL)
                .build();
        Double weightBefore = set.getWeight();
        Integer repsBefore = set.getRepetitions();

        when(setRepository.findById(set.getId())).thenReturn(Optional.of(set));

        setService.updateSet(setDto, set.getId(), user.getId());

        verify(workoutExerciseService).updateWorkoutVolume(workout, -weightBefore, repsBefore);
        verify(workoutExerciseService).updateWorkoutVolume(workout, setDto.getWeight(), setDto.getRepetitions());
        verify(setRepository).save(any(Set.class));
    }

    @Test
    @DisplayName("Update Set - expect Not Found Exception")
    public void updateSetNotFound() {
        SetDto setDto = SetDto.builder()
                .weight(12.0)
                .repetitions(11)
                .setType(SetType.NORMAL)
                .build();
        String message = "Set with id " + set.getId() + " not found";

        when(setRepository.findById(set.getId())).thenThrow(new NotFoundException(message));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            setService.updateSet(setDto, set.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Update Set - expect Bad Request")
    public void updateSetBadRequest() {
        SetDto setDto = SetDto.builder()
                .weight(12.0)
                .repetitions(11)
                .setType(SetType.NORMAL)
                .build();
        String message = "You can only update your sets!";
        when(setRepository.findById(set.getId())).thenReturn(Optional.of(set));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            setService.updateSet(setDto, set.getId(), user.getId() + 1);
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Get Set - expect success")
    public void getSetTest() {
        SetDto setDto = SetDto.builder()
                .weight(10.0)
                .repetitions(3)
                .setType(SetType.NORMAL)
                .build();
        when(setRepository.findById(set.getId())).thenReturn(Optional.of(set));

        SetDto result = setService.getSet(set.getId(), user.getId());

        assertEquals(setDto, result);
    }
}
