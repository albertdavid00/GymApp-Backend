package com.unibuc.gymapp.services;

import com.unibuc.gymapp.clients.AuthClient;
import com.unibuc.gymapp.dtos.LoginDto;
import com.unibuc.gymapp.dtos.TokenDto;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthClient authClient;
    private final UserRepository userRepository;
    @Value("${keycloak.resource}")
    private String keycloakClient;

    @Autowired
    public AuthService(AuthClient authClient, UserRepository userRepository) {
        this.authClient = authClient;
        this.userRepository = userRepository;
    }

    public TokenDto login(LoginDto loginDto) {
        Optional<User> inAppUser = userRepository.findByEmail(loginDto.getEmail());
        if (inAppUser.isEmpty()) {
            throw new NotFoundException("The user doesn't exist!");
        }

        MultiValueMap<String, String> loginCredentials = new LinkedMultiValueMap<>();
        loginCredentials.add("client_id", keycloakClient);
        loginCredentials.add("username", inAppUser.get().getId().toString());
        loginCredentials.add("password", loginDto.getPassword());
        loginCredentials.add("grant_type", loginDto.getGrantType());

        return authClient.login(loginCredentials);
    }
}
