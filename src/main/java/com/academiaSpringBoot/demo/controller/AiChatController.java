package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.dto.gemini.AiChatPromptDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "AI Chat", description = "Chat com IA personalizada para alunos da academia")
public class AiChatController {

    private final AiChatService aiChatService;

    @PostMapping
    @Operation(summary = "Enviar mensagem para a IA", description = "Envia uma mensagem para a IA considerando o perfil do usuário e o histórico da conversa.")
    public ResponseEntity<String> chat(@Valid @RequestBody AiChatPromptDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(aiChatService.generateResponse(user, dto));
    }
}
