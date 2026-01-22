package com.academiaSpringBoot.demo.dto.gemini;

import com.academiaSpringBoot.demo.model.ExperienceLevel;
import com.academiaSpringBoot.demo.model.Goal;
import com.academiaSpringBoot.demo.model.WeekDays;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO para substituir plano de treino do usuário com base nos seguintes atributos")
public record AiReplaceWorkoutPlanDTO(
        @Schema(description = "Razão da mudança de plano de treinos", example = "Tive uma torção no tornozelo")
        String reason,

        @Schema(description = "Objetivo do treino", example = "HYPERTROPHY")
        Goal goal,

        @Schema(description = "Nível de experiência", example = "INTERMEDIATE")
        ExperienceLevel experienceLevel,

        @Schema(description = "Duração do treino (em minutos)", example = "120")
        Integer durationInMinutes,

        @Schema(description = "Dias disponíveis para treinar", example = "MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY")
        List<WeekDays> availableDays) {}
