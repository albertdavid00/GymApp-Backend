package com.unibuc.gymapp.dtos;

import com.unibuc.gymapp.models.SetType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewSetDto {
    @NotNull
    @Min(value = 0, message = "The value must be positive")
    private Double weight;
    @NotNull
    @Min(value = 0, message = "The value must be positive")
    private Integer repetitions;
    @NotNull
    private SetType setType;
}
