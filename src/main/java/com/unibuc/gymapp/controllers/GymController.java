package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.GymDto;
import com.unibuc.gymapp.dtos.NewGymDto;
import com.unibuc.gymapp.services.GymService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gyms")
@Tag(name = "Gym Controller", description = "Set of endpoints for managing the gym entity.")
public class GymController {
    private final GymService gymService;
    @Autowired
    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @PostMapping
    @Operation(summary = "Allows admins to add a new gym to the application and returns the id.")
    public ResponseEntity<?> addGym(@RequestBody GymDto newGymDto, Authentication authentication) {
        return new ResponseEntity<>(gymService.addGym(newGymDto, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Allows admins to delete a gym from the application.")
    public ResponseEntity<?> removeGym(@PathVariable Long id, Authentication authentication) {
        gymService.removeGym(id, KeycloakHelper.getUserId(authentication));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping
    @Operation(summary = "Returns a list with all the gyms in the application.")
    public ResponseEntity<?> getAllGyms() {
        return new ResponseEntity<>(gymService.getAllGyms(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Returns the gym with the specified id.")
    public ResponseEntity<?> getGym(@PathVariable Long id) {
        return new ResponseEntity<>(gymService.getGym(id), HttpStatus.OK);
    }

    @GetMapping("/favorite")
    @Operation(summary = "Returns the gym where the authenticated user had most of their workouts.")
    public ResponseEntity<?> getFavoriteGym(Authentication authentication) {
        return new ResponseEntity<>(gymService.getFavoriteGym(KeycloakHelper.getUserId(authentication)), HttpStatus.OK);
    }
}
