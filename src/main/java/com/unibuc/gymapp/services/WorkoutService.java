package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.*;
import com.unibuc.gymapp.models.*;
import com.unibuc.gymapp.repositories.GymRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import com.unibuc.gymapp.repositories.WorkoutRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .volume(0.0)
                .ended(false)
                .user(user.get())
                .build();

        if (newWorkoutDto.getGymId() != null) {
            Gym gym = gymRepository.findById(newWorkoutDto.getGymId())
                    .orElseThrow(() -> new NotFoundException("Gym with id " + newWorkoutDto.getGymId() + " not found!"));
            workout.setGym(gym);
        }

        return workoutRepository.save(workout).getId();
    }

    public void finishWorkout(Long id, Long userId) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workout with id " + id + "not found!"));
        if (!workout.getUser().getId().equals(userId)) {
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

    public WorkoutDto getWorkout(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workout not found!"));

        List<CommentDto> comments = workout.getComments().stream()
                .map(comment -> CommentDto.builder()
                        .username(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                        .content(comment.getContent())
                        .creationTime(comment.getCreationTime())
                        .build())
                .collect(Collectors.toList());

        GymDto gymDto = null;
        if (workout.getGym() != null) {
            gymDto = GymDto.builder()
                    .name(workout.getGym().getName())
                    .location(workout.getGym().getLocation())
                    .program(workout.getGym().getProgram())
                    .build();
        }
        List<ExerciseDto> exercises = workout.getExercises().stream()
                .map(workoutExercise -> {
                    List<SetDto> sets = workoutExercise.getSets().stream()
                            .map(set -> SetDto.builder()
                                    .weight(set.getWeight())
                                    .repetitions(set.getRepetitions())
                                    .setType(set.getSetType())
                                    .build())
                            .collect(Collectors.toList());
                    return (Pair<Exercise, List<SetDto>>) new ImmutablePair<>(workoutExercise.getExercise(), sets);
                })
                .map(pair -> ExerciseDto.builder()
                        .title(pair.getLeft().getTitle())
                        .description(pair.getLeft().getDescription())
                        .equipmentType(pair.getLeft().getEquipmentType())
                        .targetedMuscles(pair.getLeft().getTargetedMuscles())
                        .sets(pair.getRight())
                        .build())
                .collect(Collectors.toList());

        return WorkoutDto.builder()
                .title(workout.getTitle())
                .creationTime(workout.getCreationTime())
                .durationInMinutes(workout.getDurationInMinutes())
                .volume(workout.getVolume())
                .ended(workout.isEnded())
                .comments(comments)
                .exercises(exercises)
                .gymDto(gymDto)
                .build();
    }
}
