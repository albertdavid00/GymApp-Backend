package com.unibuc.gymapp.controllers;

import com.unibuc.gymapp.dtos.CommentDto;
import com.unibuc.gymapp.services.CommentService;
import com.unibuc.gymapp.utils.KeycloakHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@Tag(name = "Set of endpoints for managing the comment entity.")
public class CommentController {
    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @GetMapping("/workout/{id}")
    @Operation(summary = "Returns a list with all the comments of a specific workout")
    public ResponseEntity<?> getCommentsForWorkout(@PathVariable Long id, Authentication authentication) {
        return new ResponseEntity<>(commentService.getCommentsForWorkout(id, KeycloakHelper.getUserId(authentication)),
                HttpStatus.OK);
    }
    @PostMapping("/{id}")
    @Operation(summary = "Adds a new comment to a specific finished workout and returns the id.")
    public ResponseEntity<?> addCommentToWorkout(@PathVariable Long id, @RequestBody CommentDto commentDto, Authentication authentication) {
        return new ResponseEntity<>(commentService.addCommentToWorkout(commentDto, id, KeycloakHelper.getUserId(authentication)),
                HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Edits an existing comment that belongs to the authenticated user.",
            description = "The content and the creation time of the comment are updated.")
    public ResponseEntity<?> editComment(@PathVariable Long id, @RequestBody CommentDto commentDto, Authentication authentication) {
        commentService.editComment(commentDto, id, KeycloakHelper.getUserId(authentication));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    @Operation(summary = "Deletes an existing comment that belongs to the authenticated user.")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, Authentication authentication) {
        commentService.deleteComment(id, KeycloakHelper.getUserId(authentication));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
