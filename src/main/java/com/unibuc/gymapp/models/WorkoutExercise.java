package com.unibuc.gymapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_exercise")
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Instant creationTime;
    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    @OneToMany(mappedBy = "workoutExercise")
    private List<Set> sets;

}
