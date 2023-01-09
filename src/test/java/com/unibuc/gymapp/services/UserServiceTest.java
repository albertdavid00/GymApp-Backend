package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.RegisterDto;
import com.unibuc.gymapp.dtos.UpdateUserDto;
import com.unibuc.gymapp.dtos.UserDto;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private KeycloakAdminService keycloakAdminService;

    private User user;
    private RegisterDto registerDto;
    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .firstName("Test")
                .lastName("Test")
                .age(13)
                .build();
        registerDto = registerDto.builder()
                .email("test@mail.com")
                .password("testpass")
                .build();
    }

    @Test
    @DisplayName("Register - expected success")
    public void register() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.register(registerDto);
        verify(userRepository).save(any(User.class));
        verify(keycloakAdminService).addUserToKeycloak(any(User.class), any(), any());

    }

    @Test
    @DisplayName("Register - expected Bad Request")
    public void registerBadRequest() {
        String message = "User with email " + registerDto.getEmail() + " already exists!";
        when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(user));

        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.register(registerDto);
        });
        Assertions.assertEquals(message, thrown.getMessage());
    }

    @Test
    @DisplayName("Get All Users - expected success")
    public void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDto> users = List.of(UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .build());
        Assertions.assertEquals(users, userService.getUsers());
    }

    @Test
    @DisplayName("Get User By Id - expect success")
    public void getUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserDto userDto = UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .build();

        Assertions.assertEquals(userDto, userService.getUser(user.getId()));
    }

    @Test
    @DisplayName("Update user - expect success")
    public void updateUser() {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .age(30).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.getById(user.getId())).thenReturn(user);
        userService.updateUser(updateUserDto, user.getId(), user.getId());
        Assertions.assertEquals(updateUserDto.getAge(), user.getAge());
    }
}
