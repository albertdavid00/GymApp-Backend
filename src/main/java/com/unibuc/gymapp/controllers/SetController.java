package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.SetDto;
import com.unibuc.gymapp.services.SetService;
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
@RequestMapping("/sets")
@Tag(name = "Set Controller", description = "Endpoints for managing the set entity.")
public class SetController {
    private final SetService setService;
    @Autowired
    public SetController(SetService setService) {
        this.setService = setService;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Allows an authenticated user to delete a set from a workout.",
    description = "Once the set is deleted, the workout's volume value will also decrease.")
    public ResponseEntity<?> deleteSet(@PathVariable Long id, Authentication authentication) {
        setService.deleteSet(id, KeycloakHelper.getUserId(authentication));
        return successResponse();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Allows an authenticated user to edit a set from a workout.",
            description = "Once the set is edited, the workout's volume value will also be updated.")
    public ResponseEntity<?> updateSet(@PathVariable Long id, @RequestBody SetDto setDto, Authentication authentication) {
        setService.updateSet(setDto, id, KeycloakHelper.getUserId(authentication));
        return successResponse();
    }
    @GetMapping("{id}")
    @Operation(summary = "Returns the set with the specified id")
    public ResponseEntity<?> getSet(@PathVariable Long id, Authentication authentication) {
        return new ResponseEntity<>(setService.getSet(id, KeycloakHelper.getUserId(authentication)),
                HttpStatus.OK);
    }
}
