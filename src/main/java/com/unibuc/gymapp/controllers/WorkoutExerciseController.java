package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.SetDto;
import com.unibuc.gymapp.services.WorkoutExerciseService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout-exercises")
@Tag(name = "Workout-Exercise Controller", description = "Set of endpoints for managing exercise operations inside an active workout.")
public class WorkoutExerciseController {
    private final WorkoutExerciseService workoutExerciseService;

    @Autowired
    public WorkoutExerciseController(WorkoutExerciseService workoutExerciseService) {
        this.workoutExerciseService = workoutExerciseService;
    }

    @PostMapping("/{workoutId}/{exerciseId}")
    @Operation(summary = "Allows the authenticated user to add a new exercise to a workout and returns the id of the newly created entity.")
    public ResponseEntity<?> addExerciseToWorkout(@PathVariable Long workoutId, @PathVariable Long exerciseId, Authentication authentication) {
        return new ResponseEntity<>(workoutExerciseService.addExerciseToWorkout(workoutId, exerciseId, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }

    @PostMapping("/add-set/{id}")
    @Operation(summary = "Adds a new set to a specific exercise inside an active workout and returns the id of the new set.",
            description = "Once the set is added to the workout, the volume is also updated.")
    public ResponseEntity<?> addSetToWorkoutExercise(@PathVariable Long id, @RequestBody SetDto newSetDto, Authentication authentication) {
        return new ResponseEntity<>(workoutExerciseService.addSetToWorkoutExercise(newSetDto, id, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
}
