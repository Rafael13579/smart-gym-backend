package com.academiaSpringBoot.demo.dto.gemini;

import com.academiaSpringBoot.demo.model.ExperienceLevel;
import com.academiaSpringBoot.demo.model.Goal;
import com.academiaSpringBoot.demo.model.WeekDays;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO para criar plano treino do usuário com base nos seguintes atributos")
public record AiGenerationRequestDTO(
        @Schema(description = "Objetivo do treino", example = "HYPERTROPHY")
        Goal goal,

        @Schema(description = "Nível de experiência", example = "INTERMEDIATE")
        ExperienceLevel experienceLevel,

        @Schema(description = "Duração do treino (em minutos)", example = "120")
        Integer durationInMinutes,

        @Schema(description = "Dias da semana disponíveis para treino", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
        List<WeekDays> availableDays) {}