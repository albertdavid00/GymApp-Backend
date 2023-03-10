package com.unibuc.gymapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;
    @ElementCollection(targetClass = MuscleGroup.class)
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> targetedMuscles;
    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises;
}
