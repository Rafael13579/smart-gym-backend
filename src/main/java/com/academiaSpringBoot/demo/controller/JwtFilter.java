package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // 1️⃣ Libera rotas públicas
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ Lê o header Authorization
        String authHeader = request.getHeader("Authorization");

        // Sem token → continua sem autenticar (Security decide depois)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 3️⃣ Extrai ID do usuário do token
            Long userId = jwtService.getUserIdFromToken(token);

            // 4️⃣ Busca usuário no banco
            User user = userRepository.findById(userId).orElse(null);

            if (user != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                // 5️⃣ Define usuário autenticado
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // Token inválido ou expirado
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
