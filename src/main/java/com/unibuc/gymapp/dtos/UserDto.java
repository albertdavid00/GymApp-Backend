package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Min(12)
    @Max(99)
    private Integer age;
}
