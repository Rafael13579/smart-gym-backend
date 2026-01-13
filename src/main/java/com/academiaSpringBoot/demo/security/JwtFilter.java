package com.academiaSpringBoot.demo.security;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("--- [DEBUG JWT] Request para: " + path);

        // Pula rotas de auth
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // 1. Verifica se o Header existe
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("--- [DEBUG JWT] Falha: Header ausente ou sem Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token
        String token = authHeader.substring(7);
        System.out.println("--- [DEBUG JWT] Token extraído: " + token);

        try {
            // 3. Tenta pegar o ID do token
            Long userId = jwtService.getUserIdFromToken(token);
            System.out.println("--- [DEBUG JWT] User ID extraído do Token: " + userId);

            if (userId != null) {
                // 4. Busca no banco Postgres
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    System.out.println("--- [DEBUG JWT] Usuário encontrado no Banco: " + user.getEmail());
                    System.out.println("--- [DEBUG JWT] Roles/Authorities: " + user.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("--- [DEBUG JWT] SUCESSO: Contexto de segurança definido!");
                } else {
                    System.out.println("--- [DEBUG JWT] ERRO: ID " + userId + " existe no token, mas NÃO existe no banco de dados.");
                }
            }
        } catch (Exception e) {
            System.out.println("--- [DEBUG JWT] EXCEPTION: " + e.getMessage());
            e.printStackTrace(); // Isso vai mostrar se o token expirou ou assinatura falhou
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}