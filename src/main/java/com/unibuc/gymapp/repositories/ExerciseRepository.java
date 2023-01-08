package com.unibuc.gymapp.repositories;

import com.unibuc.gymapp.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Optional<Exercise> findByTitle(String title);

    @Query("SELECT e FROM Exercise e WHERE e.title LIKE :title")
    List<Exercise> searchByTitle(String title);
}
