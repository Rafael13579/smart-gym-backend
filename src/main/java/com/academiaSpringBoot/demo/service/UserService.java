package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.dto.createDTO.UserCreateDTO;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.dto.responseDTO.UserResponseDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.WorkoutResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {

        if(userRepository.existsByEmail(dto.email())) {
            throw new ResourceNotFoundException("Email already exists");
        }

        User user = User.builder()
                        .name(dto.name())
                        .email(dto.email())
                        .password(passwordEncoder.encode(dto.password()))
                        .role(User.Role.USER)
                        .build();

        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getName(),
                List.of()
        );
    }

    @Transactional
    public UserResponseDTO updateName(Long userId, UserCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(dto.name() != null) {
            user.setName(dto.name());
        }

        return mapResponseToDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> listAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapResponseToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return mapResponseToDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    public UserResponseDTO mapResponseToDTO(User user) {
        List<WorkoutResponseDTO> workouts = List.of();

        if(user.getWorkouts() != null) {
            workouts = user.getWorkouts()
                    .stream()
                    .map(wk -> new WorkoutResponseDTO(
                            wk.getId(),
                            wk.getName(),
                            wk.getDay(),
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
