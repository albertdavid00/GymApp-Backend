package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.ExerciseDto;
import com.unibuc.gymapp.services.ExerciseService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;
    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<?> addExercise(@RequestBody ExerciseDto newExerciseDto, Authentication authentication) {
        return new ResponseEntity<>(exerciseService.addExercise(newExerciseDto, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchExercise(@RequestParam String title) {
        return new ResponseEntity<>(exerciseService.searchExercise(title), HttpStatus.OK);
    }
}
