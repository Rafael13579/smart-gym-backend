package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.ai.AiProvider;
import com.academiaSpringBoot.demo.dto.gemini.AiChatPromptDTO;
import com.academiaSpringBoot.demo.model.ChatMessage;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.UserProfile;
import com.academiaSpringBoot.demo.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private final AiProvider aiProvider;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public String generateResponse(User user, AiChatPromptDTO dto) {

        saveMessage(user, ChatMessage.MessageRole.USER, dto.message());

        String history = getFormattedHistory(user);

        UserProfile profile = user.getProfile();
        String finalPrompt = buildPrompt(user, profile, history, dto.message());

        String aiResponse = aiProvider.generate(finalPrompt);

        saveMessage(user, ChatMessage.MessageRole.ASSISTANT, aiResponse);

        return aiResponse;
    }

    private void saveMessage(User user, ChatMessage.MessageRole role, String content) {
        chatMessageRepository.save(ChatMessage.builder()
                .user(user)
                .role(role)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build());
    }

    private String getFormattedHistory(User user) {
        List<ChatMessage> messages = chatMessageRepository.findRecentMessages(user, PageRequest.of(0, 10));

        Collections.reverse(messages);

        return messages.stream()
                .map(m -> String.format("%s: %s", m.getRole(), m.getContent()))
                .collect(Collectors.joining("\n"));
    }

    private String buildPrompt(User user, UserProfile profile, String history, String currentMessage) {

        String userContext = (profile != null) ?
                String.format("Idade: %d, Objetivo: %s, Nível: %s", profile.getAge(), profile.getGoal(), profile.getExperienceLevel())
                : "Sem dados físicos.";

        return String.format("""
                 Você é um Personal Trainer especialista.
                 
                 DADOS DO ALUNO:
                 Nome: %s
                 %s
                 
                 HISTÓRICO DA CONVERSA (Contexto Anterior):
                 %s
                 
                 NOVA MENSAGEM DO ALUNO (Responda a isto):
                 "%s"
                 
                 Regras:
                 - Use o histórico para manter a coerência (ex: se ele disse que tem dor no joelho antes, lembre-se disso).
                 - Seja curto e objetivo.
                 - Responda apenas a nova mensagem, não repita o histórico.
                 """,
                user.getName(),
                userContext,
                history,
                currentMessage
        );
    }
}