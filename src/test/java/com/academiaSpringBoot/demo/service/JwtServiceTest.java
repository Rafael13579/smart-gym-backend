package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setup() {
        jwtService = new JwtService("minha-chave-secreta-super-segura-com-mais-de-32-caracteres", 1000 * 60 * 60);

        user = User.builder()
                .id(42L)
                .build();
    }


    @Test
    void shouldGenerateAndParseToken() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());

        Long userId = jwtService.getUserIdFromToken(token);

        assertEquals(42L, userId);
    }


    @Test
    void shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(
                Exception.class,
                () -> jwtService.getUserIdFromToken(invalidToken)
        );
    }
}
