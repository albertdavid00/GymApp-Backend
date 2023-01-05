package com.unibuc.gymapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "gyms")
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    private String location;
    private String program;
    @OneToMany(mappedBy = "gym")
    private List<Workout> workouts;
}
