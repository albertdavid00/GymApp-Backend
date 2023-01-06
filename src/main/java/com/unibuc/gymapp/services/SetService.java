package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.NewSetDto;
import com.unibuc.gymapp.models.Set;
import com.unibuc.gymapp.models.WorkoutExercise;
import com.unibuc.gymapp.repositories.SetRepository;
import com.unibuc.gymapp.repositories.WorkoutExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

@Service
public class SetService {
    private final SetRepository setRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    @Autowired
    public SetService(SetRepository setRepository, WorkoutExerciseRepository workoutExerciseRepository) {
        this.setRepository = setRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }
}
