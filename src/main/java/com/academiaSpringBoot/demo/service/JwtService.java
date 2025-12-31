package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // 🔐 Chave secreta (mínimo 256 bits)
    private static final String SECRET =
            "minha-chave-secreta-super-segura-com-mais-de-32-caracteres";

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔑 GERA TOKEN
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getId().toString()) // userId no subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔍 EXTRAI USER ID
    public Long getUserIdFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
