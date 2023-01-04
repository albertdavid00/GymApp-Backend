package com.unibuc.gymapp.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "workouts")
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private boolean ended;
    private Instant creationTime;
    private Double duration;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
    @OneToMany(mappedBy = "workout")
    private List<WorkoutExercise> exercises;
    @OneToMany(mappedBy = "workout")
    private List<Comment> comments;
}
