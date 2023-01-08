package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.GymDto;
import com.unibuc.gymapp.models.Gym;
import com.unibuc.gymapp.models.Role;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.GymRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GymService {
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    @Autowired
    public GymService(GymRepository gymRepository, UserRepository userRepository) {
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
    }

    public Long addGym(GymDto newGymDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("Only admins can make this operation!");
        }
        if (gymRepository.findByNameAndLocation(newGymDto.getName(), newGymDto.getLocation()).isPresent()) {
            throw new BadRequestException("Gym " + newGymDto.getName() + " located in " + newGymDto.getLocation() + " already exists!" );
        }
        Gym gym = Gym.builder()
                .name(newGymDto.getName())
                .location(newGymDto.getLocation())
                .program(newGymDto.getProgram())
                .build();

        return gymRepository.save(gym).getId();
    }

    public void removeGym(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!Role.ADMIN.equals(user.getRole())) {
            throw new BadRequestException("Only admins can make this operation!");
        }
        Gym gym = gymRepository.findById(id).orElseThrow(() -> new NotFoundException("Gym with id " + id + " not found!"));
        gym.getWorkouts().forEach(workout -> workout.setGym(null));
        gymRepository.delete(gym);
    }

    public List<GymDto> getAllGyms() {
        return gymRepository.findAll().stream()
                .map(gym -> GymDto.builder()
                        .name(gym.getName())
                        .location(gym.getLocation())
                        .program(gym.getProgram())
                        .build())
                .collect(Collectors.toList());
    }

    public GymDto getGym(Long id) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Gym with id " + id + " not found!"));
        return GymDto.builder()
                .name(gym.getName())
                .location(gym.getLocation())
                .program(gym.getProgram()).build();
    }

    public GymDto getFavoriteGym(Long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User not found!");
        }
        List<Gym> topGyms = gymRepository.findTopGymsForUser(userId);
        if (topGyms.isEmpty()) {
            throw new NotFoundException("User has no top gyms!");
        }
        Gym favoriteGym = topGyms.get(0);
        return GymDto.builder()
                .name(favoriteGym.getName())
                .location(favoriteGym.getLocation())
                .program(favoriteGym.getProgram())
                .build();
    }
}
