package com.academiaSpringBoot.demo.dto.gemini;

import com.academiaSpringBoot.demo.model.ExperienceLevel;
import com.academiaSpringBoot.demo.model.Goal;
import com.academiaSpringBoot.demo.model.WeekDays;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "DTO para criacao de plano de treino com IA")
public record AiGenerationRequestDTO(
        @Schema(description = "Objetivo do treino", example = "HYPERTROPHY")
        @NotNull(message = "Goal is required")
        Goal goal,

        @Schema(description = "Nivel de experiencia", example = "INTERMEDIATE")
        @NotNull(message = "Experience level is required")
        ExperienceLevel experienceLevel,

        @Schema(description = "Duracao do treino em minutos", example = "120")
        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be greater than zero")
        Integer durationInMinutes,

        @Schema(description = "Dias da semana disponiveis para treino", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
        @NotEmpty(message = "Available days are required")
        List<@NotNull(message = "Available day cannot be null") WeekDays> availableDays
) {}
