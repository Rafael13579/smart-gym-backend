package com.academiaSpringBoot.demo.createDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para criação de um exercício")
public record ExerciseCreateDTO(

        @Schema(
                description = "Nome do exercício",
                example = "Supino reto"
        )
        @NotBlank
        String name,

        @Schema(
                description = "Grupo muscular trabalhado",
                example = "Peito"
        )
        @NotBlank
        String muscularGroup,

        @Schema(
                description = "Descrição do exercício",
                example = "Exercício para fortalecimento do peitoral"
        )
        @NotBlank
        String description

) {}
