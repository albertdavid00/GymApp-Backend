package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.RegisterDto;
import com.unibuc.gymapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Contains endpoints for managing users.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Sign up a new user into the application.")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
