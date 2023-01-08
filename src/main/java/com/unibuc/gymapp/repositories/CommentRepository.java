package com.unibuc.gymapp.repositories;

import com.unibuc.gymapp.models.Comment;
import com.unibuc.gymapp.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByWorkout(Workout workout);
}
