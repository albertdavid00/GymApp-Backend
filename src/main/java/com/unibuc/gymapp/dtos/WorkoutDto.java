package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class WorkoutDto {
    @NotBlank
    private String title;
    @NotNull
    private Instant creationTime;
    private Long durationInMinutes;
    private Double volume;
    private boolean ended;
    private GymDto gymDto;
    private List<ExerciseDto> exercises;
    private List<CommentDto> comments;
}
