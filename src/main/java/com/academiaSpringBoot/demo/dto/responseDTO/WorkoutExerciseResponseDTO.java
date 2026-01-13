package com.academiaSpringBoot.demo.dto.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO de resposta que representa um exercício associado a um treino")
public record WorkoutExerciseResponseDTO(

        @Schema(
                description = "Identificador único da relação treino-exercício",
                example = "10"
        )
        Long id,

        @Schema(
                description = "Identificador do exercício",
                example = "3"
        )
        Long exerciseId,

        @Schema(
                description = "Nome do exercício",
                example = "Supino Reto"
        )
        String exerciseName,

        @Schema(
                description = "Grupo muscular trabalhado pelo exercício",
                example = "Peitoral"
        )
        String muscularGroup,

        @Schema(
                description = "Descrição do exercício",
                example = "Exercício composto focado no peitoral, tríceps e ombros"
        )
        String description,

        @Schema(
                description = "Lista de séries cadastradas para este exercício no treino"
        )
        List<TrainingSetResponseDTO> sets
) {}
