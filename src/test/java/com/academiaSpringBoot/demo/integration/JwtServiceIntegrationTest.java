package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.service.JwtService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=ChaveLegalIncrivelmenteSecretaELongaOSuficienteParaDarCertoSemNenhumaSurpresa",
        "jwt.expiration=3600000"
})
public class JwtServiceIntegrationTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void shouldGenerateAndExtractUserIdSuccessfully() {
        User user = new User();
        user.setId(12345L);
        user.setEmail("test@jwt.com");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);

        Long extractedId = jwtService.getUserIdFromToken(token);

        assertEquals(12345L, extractedId);
    }

    @Test
    void shouldThrowException_WhenTokenIsInvalid() {
        User user = new User();
        user.setId(1L);
        String token = jwtService.generateToken(user);

        String tokenAdulterado = token.substring(0, token.length() - 1) + "X";

        assertThrows(JwtException.class, () -> {
            jwtService.getUserIdFromToken(tokenAdulterado);
        });
    }

    @Test
    void shouldThrowException_WhenTokenIsMalformed() {
        String tokenLixo = "token.que.nao.existe";

        assertThrows(JwtException.class, () -> {
            jwtService.getUserIdFromToken(tokenLixo);
        });
    }

    @Test
    void shouldHandleExpiration() throws InterruptedException {
        String secret = "UmaChaveMuitoSecretaEMuitoLongaParaOAlgoritmoHS256Funcionar";
        JwtService fastExpiredService = new JwtService(secret, 1L);

        User user = new User();
        user.setId(55L);

        String token = fastExpiredService.generateToken(user);

        Thread.sleep(10);

        assertThrows(JwtException.class, () -> {
            fastExpiredService.getUserIdFromToken(token);
        });
    }
}