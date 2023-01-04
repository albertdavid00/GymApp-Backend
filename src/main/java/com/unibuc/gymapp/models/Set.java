package com.unibuc.gymapp.models;

import javax.persistence.*;

@Entity
@Table(name="sets")
public class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private SetType setType;
    private Double weight;
    private Integer repetitions;
    @ManyToOne
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;
}
