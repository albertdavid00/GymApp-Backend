package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.NewWorkoutDto;
import com.unibuc.gymapp.models.Gym;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.models.Workout;
import com.unibuc.gymapp.repositories.GymRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import com.unibuc.gymapp.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, GymRepository gymRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
    }

    public Long addWorkout(NewWorkoutDto newWorkoutDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + userId + " doesn't exist!");
        }

        Workout workout = Workout.builder()
                .title(newWorkoutDto.getTitle())
                .creationTime(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .ended(false)
                .user(user.get())
                .build();

        if (newWorkoutDto.getGymName() != null) {
            Gym gym = gymRepository.findByName(newWorkoutDto.getGymName())
                    .orElseThrow(() -> new NotFoundException("Gym named " + newWorkoutDto.getGymName() + " not found!"));
            workout.setGym(gym);
        }

        return workoutRepository.save(workout).getId();
    }

    public void finishWorkout(Long id, Long userId) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workout with id " + id + "not found!"));
        if (workout.getUser().getId() != userId) {
           throw new BadRequestException("You cannot update workouts that are not yours!");
        }
        if (workout.isEnded()) {
            throw new BadRequestException("Workout with id " + id + " already ended!");
        }
        Instant currentTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Long duration = Duration.between(workout.getCreationTime(), currentTime).toMinutes();

        workout.setDurationInMinutes(duration);
        workout.setEnded(true);
        workoutRepository.save(workout);
    }
}
