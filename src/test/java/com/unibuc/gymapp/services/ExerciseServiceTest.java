package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.ExerciseDto;
import com.unibuc.gymapp.models.*;
import com.unibuc.gymapp.repositories.ExerciseRepository;
import com.unibuc.gymapp.repositories.UserRepository;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {
    @InjectMocks
    private ExerciseService exerciseService;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserRepository userRepository;
    private User user;
    private ExerciseDto exerciseDto;
    private Exercise exercise;
    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .role(Role.ADMIN)
                .build();
        exerciseDto = ExerciseDto.builder()
                .title("Test")
                .description("Test description")
                .equipmentType(EquipmentType.BARBELL)
                .targetedMuscles(List.of(MuscleGroup.CHEST))
                .build();
        exercise = Exercise.builder()
                .id(2L)
                .title("Test")
                .description("Test description")
                .equipmentType(EquipmentType.BARBELL)
                .targetedMuscles(List.of(MuscleGroup.CHEST))
                .build();

    }

    @Test
    @DisplayName("Add exercise - expected success")
    public void addExercise() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exerciseRepository.findByTitle(exerciseDto.getTitle())).thenReturn(Optional.empty());
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Long result = exerciseService.addExercise(exerciseDto, user.getId());

        verify(exerciseRepository).save(any(Exercise.class));
        Assertions.assertEquals(exercise.getId(), result);
    }

    @Test
    @DisplayName("Add exercise - expected Not Found")
    public void addExerciseUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            exerciseService.addExercise(exerciseDto, user.getId());
        });
        Assertions.assertEquals("User not found!", thrown.getMessage());
    }

    @Test
    @DisplayName("Add exercise - expected Bad Request - ROLE")
    public void addExerciseBadRequestRole() {
        user.setRole(Role.USER);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            exerciseService.addExercise(exerciseDto, user.getId());
        });
        Assertions.assertEquals("Only admins can add new exercises!", thrown.getMessage());
    }

    @Test
    @DisplayName("Add exercise - expected Bad Request - Already exists")
    public void addExerciseAlreadyExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exerciseRepository.findByTitle(exerciseDto.getTitle())).thenReturn(Optional.of(exercise));

        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            exerciseService.addExercise(exerciseDto, user.getId());
        });
        Assertions.assertEquals("Exercise named " + exerciseDto.getTitle() + " already exists!", thrown.getMessage());
    }

    @Test
    @DisplayName("Search exercises - expected success")
    public void searchExercise() {
        List<ExerciseDto> expected = List.of(exerciseDto);
        String search = "%" + exercise.getTitle() + "%";

        when(exerciseRepository.searchByTitle(search)).thenReturn(List.of(exercise));
        List<ExerciseDto> result = exerciseService.searchExercise(exercise.getTitle());

        Assertions.assertEquals(expected, result);
    }
}
