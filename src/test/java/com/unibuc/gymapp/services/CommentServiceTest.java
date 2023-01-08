package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.CommentDto;
import com.unibuc.gymapp.models.Comment;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.repositories.CommentRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import com.unibuc.gymapp.repositories.WorkoutRepository;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private WorkoutRepository workoutRepository;
    @Mock
    private UserRepository userRepository;
    private Workout workout;
    private Comment comment;
    private User user;
    @BeforeEach
    public void setup() {
        workout = Workout.builder()
                .id(1L)
                .ended(true)
                .build();
        user = User.builder()
                .id(2L)
                .firstName("Test")
                .lastName("Last")
                .build();
        comment = Comment.builder()
                .id(3L)
                .content("This is a test.")
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .workout(workout)
                .user(user)
                .build();
    }
    @Test
    @DisplayName("Get comments for workout - expect success")
    public void getCommentsForWorkout() {
        //having
        CommentDto commentDto = CommentDto.builder()
                .content(comment.getContent())
                .creationTime(comment.getCreationTime())
                .username(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                .build();
        //when
        when(workoutRepository.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(commentRepository.findAllByWorkout(workout)).thenReturn(List.of(comment));
        List<CommentDto> result = commentService.getCommentsForWorkout(workout.getId(), user.getId());
        //then
        assertEquals(List.of(commentDto), result);
    }

    @Test
    @DisplayName("Get comments for workout - expect Not Found Exception")
    public void getCommentsForWorkoutNotFound() {
        String message = "Workout with id " + workout.getId() + " not found!";

        when(workoutRepository.findById(workout.getId())).thenThrow(new NotFoundException(message));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            commentService.getCommentsForWorkout(workout.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Add comment to workout - expect success")
    public void addCommentToWorkout() {
        CommentDto commentDto = CommentDto.builder()
                .content("This is a test.")
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .build();

        when(workoutRepository.findByIdAndEndedTrue(workout.getId())).thenReturn(Optional.of(workout));
        when(userRepository.getById(user.getId())).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Long result = commentService.addCommentToWorkout(commentDto, workout.getId(), user.getId());
        verify(userRepository).getById(user.getId());
        verify(commentRepository).save(any(Comment.class));
        assertEquals(comment.getId(), result);
    }

    @Test
    @DisplayName("Add comment to workout - expect Not Found Exception")
    public void addCommentToWorkoutNotFound() {
        CommentDto commentDto = CommentDto.builder()
                .content("This is a test.")
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        String message = "Finished workout with id " + workout.getId() + " not found!";

        when(workoutRepository.findByIdAndEndedTrue(workout.getId())).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            commentService.addCommentToWorkout(commentDto, workout.getId(), user.getId());
        });

        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Edit comment - expect success")
    public void editComment() {
        CommentDto commentDto = CommentDto.builder()
                .content("Edit")
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .build();

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        commentService.editComment(commentDto, comment.getId(), user.getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("Edit comment - expect Not Found Exception")
    public void editCommentNotFound() {
        CommentDto commentDto = CommentDto.builder()
                .content("Edit")
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        String message = "Comment with id " + comment.getId() + " not found!";

        when(commentRepository.findById(comment.getId())).thenThrow(new NotFoundException(message));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            commentService.editComment(commentDto, comment.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Edit comment - expect Bad Request Exception")
    public void editCommentOtherUser() {
        CommentDto commentDto = CommentDto.builder()
                .content("Edit")
                .creationTime(Instant.now().truncatedTo(ChronoUnit.MINUTES))
                .build();
        String message = "You can't edit comments of other users!";

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            commentService.editComment(commentDto, comment.getId(), user.getId() + 1);
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Delete comment - expect success")
    public void deleteComment() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));

        commentService.deleteComment(comment.getId(), user.getId());
        verify(commentRepository).delete(any(Comment.class));
    }

    @Test
    @DisplayName("Delete comment - expect Not Found Exception")
    public void deleteCommentNotFound() {
        String message = "Comment with id " + comment.getId() + " not found!";

        when(commentRepository.findById(comment.getId())).thenThrow(new NotFoundException(message));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            commentService.deleteComment(comment.getId(), user.getId());
        });
        assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Delete comment - expect Bad Request Exception")
    public void deleteCommentBadRequest() {
        String message = "You can't delete comments of other users!";

        when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            commentService.deleteComment(comment.getId(), user.getId() + 1);
        });
        assertEquals(message, thrown.getMessage());
    }
}
