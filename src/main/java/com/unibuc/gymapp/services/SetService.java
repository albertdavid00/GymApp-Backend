package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.SetDto;
import com.unibuc.gymapp.models.Set;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.repositories.SetRepository;
import com.unibuc.gymapp.repositories.WorkoutExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@Service
public class SetService {
    private final SetRepository setRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutExerciseService workoutExerciseService;
    @Autowired
    public SetService(SetRepository setRepository, WorkoutExerciseRepository workoutExerciseRepository, WorkoutExerciseService workoutExerciseService) {
        this.setRepository = setRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutExerciseService = workoutExerciseService;
    }

    @Transactional
    public void deleteSet(Long id, Long userId) {
        Set set = setRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Set with id " + id + " not found"));
        User user = set.getWorkoutExercise().getWorkout().getUser();
        if (!user.getId().equals(userId)) {
            throw new BadRequestException("You can only delete your sets!");
        }
        Workout workout = set.getWorkoutExercise().getWorkout();

        workoutExerciseService.updateWorkoutVolume(workout, -set.getWeight(), set.getRepetitions());
        setRepository.delete(set);
    }

    @Transactional
    public void updateSet(SetDto setDto, Long id, Long userId) {
        Set set = setRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Set with id " + id + " not found"));
        User user = set.getWorkoutExercise().getWorkout().getUser();
        if (!user.getId().equals(userId)) {
            throw new BadRequestException("You can only update your sets!");
        }
        Workout workout = set.getWorkoutExercise().getWorkout();
        workoutExerciseService.updateWorkoutVolume(workout, -set.getWeight(), set.getRepetitions());
        set.setSetType(setDto.getSetType());
        set.setWeight(setDto.getWeight());
        set.setRepetitions(setDto.getRepetitions());
        workoutExerciseService.updateWorkoutVolume(workout, setDto.getWeight(), setDto.getRepetitions());
        setRepository.save(set);
    }
}
