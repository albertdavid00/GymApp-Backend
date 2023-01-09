package com.unibuc.gymapp.services;

import com.unibuc.gymapp.dtos.RegisterDto;
import com.unibuc.gymapp.dtos.UpdateUserDto;
import com.unibuc.gymapp.dtos.UserDto;
import com.unibuc.gymapp.models.Role;
import com.unibuc.gymapp.models.User;
import com.unibuc.gymapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDto.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .age(user.getAge())
                        .build())
                .collect(Collectors.toList());
    }
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .build();
    }

    public void updateUser(UpdateUserDto updateUserDto, Long id, Long userId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        User currentUser = userRepository.getById(userId);
        if (!Role.ADMIN.equals(currentUser.getRole()) && !id.equals(userId)) {
            throw new BadRequestException("You can't update other users' information!");
        }
        if (updateUserDto.getFirstName() != null && !updateUserDto.getFirstName().isBlank()) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null && !updateUserDto.getLastName().isBlank()) {
            user.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().isBlank()) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getAge() != null && updateUserDto.getAge() < 100 && updateUserDto.getAge() > 12) {
            user.setAge(updateUserDto.getAge());
        }
        userRepository.save(user);
    }
}
