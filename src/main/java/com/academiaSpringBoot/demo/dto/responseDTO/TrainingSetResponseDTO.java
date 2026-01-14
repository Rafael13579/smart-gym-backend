package com.academiaSpringBoot.demo.dto.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta representando uma série de treino")
public record TrainingSetResponseDTO(

        @Schema(description = "Identificador único da série", example = "42")
        Long id,

        @Schema(description = "Carga utilizada na série (em kg)", example = "80.0")
        Double weight,

        @Schema(description = "Quantidade de repetições executadas", example = "10")
        Integer reps
) {}
