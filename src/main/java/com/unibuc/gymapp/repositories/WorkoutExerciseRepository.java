package com.unibuc.gymapp.repositories;

import com.unibuc.gymapp.models.Exercise;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.models.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutExerciseRepository  extends JpaRepository<WorkoutExercise, Long> {
    Optional<WorkoutExercise> findByWorkoutAndExercise(Workout workout, Exercise exercise);
}
