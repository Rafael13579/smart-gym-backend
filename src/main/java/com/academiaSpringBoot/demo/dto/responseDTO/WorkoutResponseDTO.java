package com.academiaSpringBoot.demo.dto.responseDTO;

import com.academiaSpringBoot.demo.model.WeekDays;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de resposta que representa um treino do usuário")
public record WorkoutResponseDTO(

        @Schema(
                description = "Identificador único do treino",
                example = "5"
        )
        Long id,

        @Schema(
                description = "Nome do treino",
                example = "Treino A - Peito e Tríceps"
        )
        String name,

        @Schema(
                description = "Dia da semana em que o treino é realizado",
                example = "MONDAY"
        )
        WeekDays day,

        @Schema(
                description = "Lista de exercícios associados a este treino"
        )
        List<WorkoutExerciseResponseDTO> exercises
) {}
