package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.createDTO.UserCreateDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.responseDTO.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.academiaSpringBoot.demo.model.User.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreateDTO createDTO;
    private User user;

    @BeforeEach
    void setup() {
        createDTO = new UserCreateDTO(
                "John Doe",
                "john@test.com",
                "123456",
                USER
        );

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@test.com")
                .password("encoded-password")
                .role(USER)
                .build();
    }


    @Test
    void shouldCreateUserSuccessfully() {
        when(userRepository.existsByEmail("john@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDTO response = userService.createUser(createDTO);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("john@test.com", response.email());
        assertEquals("John Doe", response.name());
        assertTrue(response.workouts().isEmpty());

        verify(userRepository, times(1))
                .existsByEmail("john@test.com");

        verify(passwordEncoder, times(1))
                .encode("123456");

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("john@test.com"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(createDTO));

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }


    @Test
    void shouldListAllUsers() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.listAllUsers();

        assertEquals(1, result.size());
        assertEquals("john@test.com", result.getFirst().email());
    }


    @Test
    void shouldGetUserByIdSuccessfully() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertEquals(1L, response.id());
        assertEquals("John Doe", response.name());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));

        assertEquals("User not found", exception.getMessage());
    }


    @Test
    void shouldMapUserWithoutWorkouts() {
        UserResponseDTO response = userService.mapResponseToDTO(user);

        assertNotNull(response);
        assertTrue(response.workouts().isEmpty());
    }
}
