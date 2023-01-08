package com.unibuc.gymapp.services;

import com.unibuc.gymapp.clients.AuthClient;
import com.unibuc.gymapp.dtos.LoginDto;
import com.unibuc.gymapp.dtos.TokenDto;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthClient authClient;
    @Mock
    private UserRepository userRepository;
    private User user;
    private LoginDto loginDto;
    private TokenDto tokenDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .build();
        loginDto = LoginDto.builder()
                .email("test@mail.com")
                .password("testpass")
                .grantType("password")
                .build();
        tokenDto = TokenDto.builder()
                .accessToken("test-token")
                .build();
    }

    @Test
    @DisplayName("Login - expected success")
    public void login() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(authClient.login(any(MultiValueMap.class))).thenReturn(tokenDto);

        TokenDto result = authService.login(loginDto);
        verify(userRepository).findByEmail(loginDto.getEmail());
        verify(authClient).login(any(MultiValueMap.class));
        assertEquals(tokenDto, result);
    }

    @Test
    @DisplayName("Login - expected Not Found")
    public void loginNotFound() {
        String message = "The user doesn't exist!";

        when(userRepository.findByEmail(loginDto.getEmail())).thenThrow(new NotFoundException(message));

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            authService.login(loginDto);
        });
        assertEquals(message, thrown.getMessage());
    }
}
