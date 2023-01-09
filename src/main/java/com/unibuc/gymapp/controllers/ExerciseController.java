package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.ExerciseDto;
import com.unibuc.gymapp.services.ExerciseService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercise Controller", description = "Set of endpoints for managing the exercise entity.")
public class ExerciseController {
    private final ExerciseService exerciseService;
    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    @Operation(summary = "Allows admins to add a new exercise in the application and returns the id.")
    public ResponseEntity<?> addExercise(@RequestBody ExerciseDto newExerciseDto, Authentication authentication) {
        return new ResponseEntity<>(exerciseService.addExercise(newExerciseDto, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
    @GetMapping("/filter")
    @Operation(summary = "Filters the exercises with the title containing the specified string and returns a list.")
    public ResponseEntity<?> filterExercise(@RequestParam String title) {
        return new ResponseEntity<>(exerciseService.filterExercises(title), HttpStatus.OK);
    }
}
