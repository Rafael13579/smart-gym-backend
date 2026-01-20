package com.academiaSpringBoot.demo.ai;

import com.academiaSpringBoot.demo.dto.gemini.GeminiRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.GeminiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiAiProvider implements AiProvider {

    private final GeminiClient geminiClient;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Override
    public String generate(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Chave da API Gemini não configurada (gemini.api.key)");
        }

        GeminiRequestDTO request = GeminiRequestDTO.fromPrompt(prompt);
        GeminiResponseDTO response = geminiClient.generate(apiKey, request);

        if (response == null || response.candidates() == null || response.candidates().isEmpty()) {
            throw new IllegalStateException("Resposta vazia da Gemini");
        }

        var candidate = response.candidates().getFirst();
        if (candidate.content() == null
                || candidate.content().parts() == null
                || candidate.content().parts().isEmpty()) {
            throw new IllegalStateException("Conteúdo inválido retornado pela Gemini");
        }

        return candidate.content().parts().getFirst().text();
    }
}
