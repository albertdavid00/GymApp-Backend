package com.unibuc.gymapp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
}
