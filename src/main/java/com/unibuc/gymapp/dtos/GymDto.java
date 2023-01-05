package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class GymDto {

    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotBlank
    private String program;
}
