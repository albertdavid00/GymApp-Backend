package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.SetDto;
import com.unibuc.gymapp.models.Exercise;
import com.unibuc.gymapp.models.Set;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.models.WorkoutExercise;
import com.unibuc.gymapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class WorkoutExerciseService {
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final SetRepository setRepository;
    @Autowired
    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository,
                                  UserRepository userRepository,
                                  WorkoutRepository workoutRepository,
                                  ExerciseRepository exerciseRepository, SetRepository setRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
    }

    public Long addExerciseToWorkout(Long workoutId, Long exerciseId, Long userId) {
        Workout workout = workoutRepository.findActiveWorkoutOfUser(workoutId, userId)
                .orElseThrow(() -> new NotFoundException("Active workout with id " + workoutId + " not found!"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("Exercise with id " + exerciseId + " not found!"));
        if (workoutExerciseRepository.findByWorkoutAndExercise(workout, exercise).isPresent()) {
            throw new BadRequestException("Exercise already in workout!");
        }
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .creationTime(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .workout(workout)
                .exercise(exercise)
                .build();

        return workoutExerciseRepository.save(workoutExercise).getId();
    }

    @Transactional
    public Long addSetToWorkoutExercise(SetDto newSetDto, Long id, Long userId) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workout exercise with id " + id + " not found!"));
        if (workoutExercise.getWorkout().isEnded() || !workoutExercise.getWorkout().getUser().getId().equals(userId)) {
            throw new BadRequestException("Invalid workout!");
        }
        Set set = Set.builder()
                .weight(newSetDto.getWeight())
                .repetitions(newSetDto.getRepetitions())
                .setType(newSetDto.getSetType())
                .workoutExercise(workoutExercise)
                .build();

        Long setId = setRepository.save(set).getId();
        updateWorkoutVolume(workoutExercise.getWorkout(), newSetDto.getWeight(), newSetDto.getRepetitions());

        return setId;
    }
    protected void updateWorkoutVolume(Workout workout, Double weight, Integer reps) {
        Double updatedVolume = workout.getVolume() + weight * reps;
        workout.setVolume(updatedVolume);
        workoutRepository.save(workout);
    }

    public void deleteWorkoutExercise(Long id, Long userId) {
        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workout exercise not found!"));
        if (!workoutExercise.getWorkout().getUser().getId().equals(userId)){
            throw new BadRequestException("You can't delete exercises of workouts that are not yours!");
        }
        workoutExerciseRepository.delete(workoutExercise);
    }
}
