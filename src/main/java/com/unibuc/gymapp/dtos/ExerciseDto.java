package com.unibuc.gymapp.dtos;

import com.unibuc.gymapp.models.EquipmentType;
import com.unibuc.gymapp.models.MuscleGroup;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ExerciseDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private EquipmentType equipmentType;
    @NotNull
    private List<MuscleGroup> targetedMuscles;
    private List<SetDto> sets;
}
