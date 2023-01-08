package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.NewWorkoutDto;
import com.unibuc.gymapp.services.WorkoutService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.unibuc.gymapp.utils.HttpStatusUtility.successResponse;

@RestController
@RequestMapping("/workouts")
@Tag(name = "Workout Controller", description = "Set of endpoints for managing the workout entity.")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    @Operation(summary = "Allows an authenticated user to create a new empty workout and returns the id of it.")
    public ResponseEntity<?> addWorkout(@RequestBody NewWorkoutDto newWorkoutDto, Authentication authentication) {
        return new ResponseEntity<>(workoutService.addWorkout(newWorkoutDto, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Allows an authenticated user to change the state of the workout to finished.",
            description = "After finishing the workout, the duration (in minutes) is calculated automatically.")
    public ResponseEntity<?> finishWorkout(@PathVariable Long id, Authentication authentication) {
        workoutService.finishWorkout(id, KeycloakHelper.getUserId(authentication));
        return successResponse();
    }
}
