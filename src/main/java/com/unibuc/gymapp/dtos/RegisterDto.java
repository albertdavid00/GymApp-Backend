package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterDto {

    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 6, message = "Password should be at least 6 characters!")
    private String password;
    @Min(12)
    @Max(99)
    private Integer age;
}
