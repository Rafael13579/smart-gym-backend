package com.academiaSpringBoot.demo.dto.gemini;

import io.swagger.v3.oas.annotations.media.Schema;

public record AiChatPromptDTO(

        @Schema(description = "Prompt escrito pelo usuário", example = "como realizar o agachamento livre?")
        String message
) {}
