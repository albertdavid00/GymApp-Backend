package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.ExerciseDto;
import com.unibuc.gymapp.dtos.UpdateExerciseDto;
import com.unibuc.gymapp.models.Exercise;
import com.unibuc.gymapp.models.Role;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.ExerciseRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    public Long addExercise(ExerciseDto newExerciseDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        if (!Role.ADMIN.equals(user.getRole())) {
            throw new BadRequestException("Only admins can add new exercises!");
        }
        if (exerciseRepository.findByTitle(newExerciseDto.getTitle()).isPresent()) {
            throw new BadRequestException("Exercise named " + newExerciseDto.getTitle() + " already exists!");
        }
        Exercise exercise = Exercise.builder()
                .title(newExerciseDto.getTitle())
                .description(newExerciseDto.getDescription())
                .equipmentType(newExerciseDto.getEquipmentType())
                .targetedMuscles(newExerciseDto.getTargetedMuscles())
                .build();
        return exerciseRepository.save(exercise).getId();
    }

    public List<ExerciseDto> searchExercise(String title) {
        List<Exercise> exercises = exerciseRepository.searchByTitle("%" + title + "%");
        return exercises.stream()
                .map(exercise -> ExerciseDto.builder()
                        .title(exercise.getTitle())
                        .description(exercise.getDescription())
                        .equipmentType(exercise.getEquipmentType())
                        .targetedMuscles(exercise.getTargetedMuscles())
                        .build())
                .collect(Collectors.toList());
    }

    public void updateExercise(UpdateExerciseDto updateExerciseDto, Long id, Long userId) {
        if (!Role.ADMIN.equals(userRepository.getById(userId).getRole())) {
            throw new BadRequestException("Only admins can make this operation!");
        }
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exercise not found!"));
        if (updateExerciseDto.getTitle() != null && !updateExerciseDto.getTitle().isBlank()){
            exercise.setTitle(updateExerciseDto.getTitle());
        }
        if (updateExerciseDto.getDescription() != null && !updateExerciseDto.getDescription().isBlank()) {
            exercise.setDescription(updateExerciseDto.getDescription());
        }
        if (updateExerciseDto.getEquipmentType() != null) {
            exercise.setEquipmentType(updateExerciseDto.getEquipmentType());
        }
        if (updateExerciseDto.getTargetedMuscles() != null && !updateExerciseDto.getTargetedMuscles().isEmpty()) {
            exercise.setTargetedMuscles(updateExerciseDto.getTargetedMuscles());
        }
        exerciseRepository.save(exercise);
    }
}
