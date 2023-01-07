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
    @GetMapping("/search")
    @Operation(summary = "Search for the exercise with the specified title and return it.")
    public ResponseEntity<?> searchExercise(@RequestParam String title) {
        return new ResponseEntity<>(exerciseService.searchExercise(title), HttpStatus.OK);
    }
}
