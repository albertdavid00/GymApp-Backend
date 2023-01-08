package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.GymDto;
import com.unibuc.gymapp.models.Gym;
import com.unibuc.gymapp.models.Role;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.GymRepository;
import com.unibuc.gymapp.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GymServiceTest {

    @InjectMocks
    private GymService gymService;
    @Mock
    private GymRepository gymRepository;
    @Mock
    private UserRepository userRepository;
    private User user;
    private Gym gym;
    private GymDto gymDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .role(Role.ADMIN)
                .firstName("test")
                .lastName("user")
                .email("testuser@mail.com")
                .age(20)
                .build();
        gym = Gym.builder()
                .id(2L)
                .name("gymTest")
                .program("24/7")
                .location("TestTown")
                .workouts(List.of()).build();

        gymDto = GymDto.builder()
                .name("gymTest")
                .program("24/7")
                .location("TestTown").build();
    }

    @Test
    @DisplayName("Add Gym, expected success")
    public void addGym() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(gymRepository.findByNameAndLocation(gymDto.getName(), gymDto.getLocation())).thenReturn(Optional.empty());
        when(gymRepository.save(any(Gym.class))).thenReturn(gym);

        Long result = gymService.addGym(gymDto, user.getId());
        verify(gymRepository).findByNameAndLocation(gymDto.getName(), gymDto.getLocation());
        verify(gymRepository).save(any(Gym.class));
        assertEquals(gym.getId(), result);
    }

    @Test
    @DisplayName("Add Gym, expected User not found")
    public void addGymUserNotFound() {
        //having
        GymDto newGymDto = GymDto.builder()
                .name("gymTest")
                .program("test")
                .location("Test").build();

        //when
        when(userRepository.findById(1L)).thenThrow((new NotFoundException("User not found")));

        //then
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            gymService.addGym(newGymDto, 1L);
        });

        verify(userRepository).findById(1L);
        Assertions.assertEquals("User not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Add Gym - already exists exception")
    public void addGymAlreadyExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(gymRepository.findByNameAndLocation(gymDto.getName(), gymDto.getLocation())).thenReturn(Optional.ofNullable(gym));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            gymService.addGym(gymDto, user.getId());
        });
        Assertions.assertEquals("Gym " + gymDto.getName() + " located in " + gymDto.getLocation() + " already exists!", thrown.getMessage());
    }

    @Test
    @DisplayName("Remove gym - expect success")
    public void removeGym() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(gymRepository.findById(gym.getId())).thenReturn(Optional.ofNullable(gym));

        gymService.removeGym(gym.getId(), user.getId());
        verify(gymRepository).delete(gym);
    }

    @Test
    @DisplayName("Remove Gym - Role Admin exception")
    public void removeGymBadRequest() {
        User user2 = User.builder()
                .id(3L)
                .role(Role.USER).build();
        when(userRepository.findById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            gymService.removeGym(gym.getId(), user2.getId());
        });
        assertEquals("Only admins can make this operation!", thrown.getMessage());
    }

    @Test
    @DisplayName("Get all gyms")
    public void getAllGyms() {
        // having
        List<Gym> gyms = List.of(gym);
        List<GymDto> gymDtos = List.of(gymDto);
        // when
        when(gymRepository.findAll()).thenReturn(gyms);
        //then
        List<GymDto> result = gymService.getAllGyms();

        assertEquals(gymDtos, result);
    }

    @Test
    @DisplayName("Get gym expect success")
    public void getGym() {
        //when
        when(gymRepository.findById(gym.getId())).thenReturn(Optional.ofNullable(gym));
        //then
        GymDto result = gymService.getGym(gym.getId());
        assertEquals(gymDto, result);
    }

    @Test
    @DisplayName("Get Gym expect not found exception")
    public void getGymNotFound() {
        when(gymRepository.findById(gym.getId())).thenThrow(new NotFoundException("Gym with id " + gym.getId() + " not found!"));
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            gymService.getGym(gym.getId());
        });
        assertEquals("Gym with id " + gym.getId() + " not found!", thrown.getMessage());
    }

    @Test
    @DisplayName("Get Favorite Gym expect success")
    public void getFavoriteGym() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(gymRepository.findTopGymsForUser(user.getId())).thenReturn(List.of(gym));

        GymDto result = gymService.getFavoriteGym(user.getId());
        assertEquals(gymDto, result);
    }

    @Test
    @DisplayName("Get Favorite Gym expect NotFoundException")
    public void getFavoriteGymNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(gymRepository.findTopGymsForUser(user.getId())).thenReturn(List.of());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            gymService.getFavoriteGym(user.getId());
        });
        assertEquals("User has no top gyms!", thrown.getMessage());
    }
}
