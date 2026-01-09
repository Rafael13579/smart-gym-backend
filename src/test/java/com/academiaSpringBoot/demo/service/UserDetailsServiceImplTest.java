package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1L)
                .email("user@test.com")
                .password("encoded-password")
                .role(User.Role.USER)
                .build();
    }


    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                userDetailsService.loadUserByUsername("user@test.com");

        assertNotNull(result);
        assertEquals("user@test.com", result.getUsername());
        assertEquals("encoded-password", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1))
                .findByEmail("user@test.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("user@test.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("user@test.com"));

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(userRepository, times(1))
                .findByEmail("user@test.com");
    }
}
