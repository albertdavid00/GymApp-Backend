package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.RegisterDto;
import com.unibuc.gymapp.models.Role;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Autowired
    public UserService(UserRepository userRepository, KeycloakAdminService keycloakAdminService) {
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    @Transactional
    public void register(RegisterDto registerDto) {
        Optional<User> inAppUser = userRepository.findByEmail(registerDto.getEmail());
        if (inAppUser.isPresent()) {
            throw new BadRequestException("User with email " + registerDto.getEmail() + " already exists!");
        }
        User newUser = User.builder()
                .email(registerDto.getEmail())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .age(registerDto.getAge())
                .role(Role.USER)
                .build();
        newUser = userRepository.save(newUser);

        keycloakAdminService.addUserToKeycloak(newUser, registerDto.getPassword(), String.valueOf(Role.USER));
    }
}
