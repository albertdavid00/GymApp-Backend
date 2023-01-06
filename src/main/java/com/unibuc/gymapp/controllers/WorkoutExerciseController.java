package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.NewSetDto;
import com.unibuc.gymapp.services.WorkoutExerciseService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout-exercises")
public class WorkoutExerciseController {
    private final WorkoutExerciseService workoutExerciseService;

    @Autowired
    public WorkoutExerciseController(WorkoutExerciseService workoutExerciseService) {
        this.workoutExerciseService = workoutExerciseService;
    }

    @PostMapping("/{workoutId}/{exerciseId}")
    public ResponseEntity<?> addExerciseToWorkout(@PathVariable Long workoutId, @PathVariable Long exerciseId, Authentication authentication) {
        return new ResponseEntity<>(workoutExerciseService.addExerciseToWorkout(workoutId, exerciseId, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }

    @PostMapping("/add-set/{id}")
    public ResponseEntity<?> addSetToWorkoutExercise(@PathVariable Long id, @RequestBody NewSetDto newSetDto, Authentication authentication) {
        return new ResponseEntity<>(workoutExerciseService.addSetToWorkoutExercise(newSetDto, id, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
}
