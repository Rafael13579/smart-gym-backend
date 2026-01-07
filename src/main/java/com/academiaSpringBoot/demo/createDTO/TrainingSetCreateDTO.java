package com.academiaSpringBoot.demo.createDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Dados para criação de uma série de treino")
public record TrainingSetCreateDTO (

        @Schema(
                description = "Carga utilizada no exercício (em kg)",
                example = "40.5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @PositiveOrZero(message = "Weight must be zero or greater") Double weight,

        @Schema(
                description = "Quantidade de repetições",
                example = "12",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Min(value = 1, message = "Reps must be at least 1") Integer reps)
{}
