package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.NewGymDto;
import com.unibuc.gymapp.services.GymService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gyms")
public class GymController {
    private final GymService gymService;
    @Autowired
    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @PostMapping
    public ResponseEntity<?> addGym(@RequestBody NewGymDto newGymDto, Authentication authentication) {
        return new ResponseEntity<>(gymService.addGym(newGymDto, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeGym(@PathVariable Long id, Authentication authentication) {
        gymService.removeGym(id, KeycloakHelper.getUserId(authentication));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<?> getAllGyms() {
        return new ResponseEntity<>(gymService.getAllGyms(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getGym(@PathVariable Long id) {
        return new ResponseEntity<>(gymService.getGym(id), HttpStatus.OK);
    }
}
