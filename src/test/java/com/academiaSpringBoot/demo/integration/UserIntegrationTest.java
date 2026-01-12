package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.createDTO.UserCreateDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUser() {
        UserCreateDTO dto = new UserCreateDTO(
                "Rafael",
                "rafael123@gmail.com",
                "123",
                User.Role.USER);

        var response = userService.createUser(dto);

        User user = userRepository.findById(response.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        assertEquals("Rafael", user.getName());
        assertTrue(userRepository.existsByEmail("rafael123@gmail.com"));
    }

    @Test
    void shouldDeleteUser() {
        UserCreateDTO dto = new UserCreateDTO(
                "Rafael",
                "rafael123@gmail.com",
                "123",
                User.Role.USER);

        var response = userService.createUser(dto);

        Long id = response.id();

        userService.deleteUser(id);

        assertFalse(userRepository.findById(id).isPresent());
    }

    @Test
    void shouldUpdateName() {
        UserCreateDTO dto = new UserCreateDTO(
                "Rafael",
                "rafael123@gmail.com",
                "123",
                User.Role.USER);

        var response = userService.createUser(dto);

        User user = userRepository.findById(response.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UserCreateDTO novoNome = new UserCreateDTO(
                "Geovana",
                null,
                null,
                null);

        userService.updateName(response.id(), novoNome);

        assertEquals("Geovana", user.getName());
        assertEquals("rafael123@gmail.com", user.getEmail());
    }
}
