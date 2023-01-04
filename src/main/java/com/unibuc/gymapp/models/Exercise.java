package com.unibuc.gymapp.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;
    @ElementCollection(targetClass = MuscleGroup.class)
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> targetedMuscles;
    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises;
}
