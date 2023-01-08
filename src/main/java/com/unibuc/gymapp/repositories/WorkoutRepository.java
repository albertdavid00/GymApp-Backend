package com.unibuc.gymapp.repositories;

import com.unibuc.gymapp.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("SELECT w FROM Workout w WHERE w.id = :workoutId AND w.user.id = :userId AND w.ended = FALSE")
    Optional<Workout> findActiveWorkoutOfUser(Long workoutId, Long userId);

    Optional<Workout> findByIdAndEndedTrue(Long id);
}
