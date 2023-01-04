package com.unibuc.gymapp.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Integer age;
    @OneToMany(mappedBy = "user")
    private List<Workout> workouts;
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
}
