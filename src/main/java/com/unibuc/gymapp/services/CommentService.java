package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.CommentDto;
import com.unibuc.gymapp.models.Comment;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.repositories.CommentRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import com.unibuc.gymapp.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository, WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public List<CommentDto> getCommentsForWorkout(Long id, Long userId) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workout with id " + id + " not found!"));
        List<Comment> comments = commentRepository.findAllByWorkout(workout);
        return comments.stream()
                .map(comment -> CommentDto.builder()
                        .content(comment.getContent())
                        .creationTime(comment.getCreationTime())
                        .username(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                        .build())
                .collect(Collectors.toList());
    }

    public Long addCommentToWorkout(CommentDto commentDto, Long id, Long userId) {
        Workout endedWorkout = workoutRepository.findByIdAndEndedTrue(id)
                .orElseThrow(() -> new NotFoundException("Finished workout with id " + id + " not found!"));

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .workout(endedWorkout)
                .user(userRepository.getById(userId))
                .build();

        return commentRepository.save(comment).getId();
    }

    public void editComment(CommentDto commentDto, Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found!"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can't edit comments of other users!");
        }
        comment.setContent(commentDto.getContent());
        comment.setCreationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES));

        commentRepository.save(comment);
    }

    public void deleteComment(Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found!"));
        if (!comment.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can't delete comments of other users!");
        }
        commentRepository.delete(comment);
    }
}
