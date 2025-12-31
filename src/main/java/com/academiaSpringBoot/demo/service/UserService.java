package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.createDTO.UserCreateDTO;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.responseDTO.UserResponseDTO;
import com.academiaSpringBoot.demo.responseDTO.WorkoutResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(UserCreateDTO dto) {

        if(userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                        .name(dto.name())
                        .email(dto.email())
                        .password(passwordEncoder.encode(dto.password()))
                        .role(User.Role.USER)
                        .build();

        User saved = userRepository.save(user);

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                List.of()
        );
    }

    public List<UserResponseDTO> listAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapResponseToDTO)
                .toList();
    }

    public UserResponseDTO getUserById(Long id) {
        return mapResponseToDTO(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponseDTO mapResponseToDTO(User user) {
        List<WorkoutResponseDTO> workouts = List.of();

        if(user.getWorkouts() != null) {
            workouts = user.getWorkouts()
                    .stream()
                    .map(wk -> new WorkoutResponseDTO(
                            wk.getId(),
                            wk.getName(),
                            List.of()
                    ))
                    .toList();
        }

            return new UserResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    workouts
            );

    }

}
