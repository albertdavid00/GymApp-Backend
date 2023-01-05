package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.NewWorkoutDto;
import com.unibuc.gymapp.services.WorkoutService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<?> addWorkout(@RequestBody NewWorkoutDto newWorkoutDto, Authentication authentication) {
        return new ResponseEntity<>(workoutService.addWorkout(newWorkoutDto, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> finishWorkout(@PathVariable Long id, Authentication authentication) {
        workoutService.finishWorkout(id, KeycloakHelper.getUserId(authentication));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
