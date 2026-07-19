package com.academiaSpringBoot.demo.dto.gemini;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para mensagem enviada ao chat com IA")
public record AiChatPromptDTO(
        @Schema(description = "Mensagem escrita pelo usuario", example = "como realizar o agachamento livre?")
        @NotBlank(message = "Message is required")
        @Size(max = 1000, message = "Message must have at most 1000 characters")
        String message
) {}
