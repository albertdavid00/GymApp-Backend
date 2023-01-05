package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class NewWorkoutDto {

    @NotBlank
    private String title;
    private String gymName;
}
