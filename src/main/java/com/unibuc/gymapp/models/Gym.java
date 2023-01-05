package com.unibuc.gymapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "gyms")
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotBlank
    private String program;
    @OneToMany(mappedBy = "gym")
    private List<Workout> workouts;
}
