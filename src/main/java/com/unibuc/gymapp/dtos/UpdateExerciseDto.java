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
public class UpdateExerciseDto {
    private String title;
    private String description;
    private EquipmentType equipmentType;
    private List<MuscleGroup> targetedMuscles;
}
