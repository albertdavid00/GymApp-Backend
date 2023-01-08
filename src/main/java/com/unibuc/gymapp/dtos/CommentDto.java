package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Builder
public class CommentDto {
    @NotBlank
    private String content;
    private String username;
    private Instant creationTime;
}
