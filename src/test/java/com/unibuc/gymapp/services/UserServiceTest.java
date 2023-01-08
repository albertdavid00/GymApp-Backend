package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.RegisterDto;
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

}
