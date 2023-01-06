package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.NewSetDto;
import com.unibuc.gymapp.services.SetService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sets")
public class SetController {
    private final SetService setService;
    @Autowired
    public SetController(SetService setService) {
        this.setService = setService;
    }
}
