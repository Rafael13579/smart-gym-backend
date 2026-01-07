package com.academiaSpringBoot.demo.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com os dados do exercício")
public record ExerciseResponseDTO(

        @Schema(
                description = "Identificador único do exercício",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome do exercício",
                example = "Supino Reto"
        )
        String name,

        @Schema(
                description = "Grupo muscular trabalhado",
                example = "Peito"
        )
        String muscularGroup,

        @Schema(
                description = "Descrição do exercício",
                example = "Exercício para peitoral maior utilizando barra"
        )
        String description,

        @Schema(
                description = "URL da imagem ou thumbnail do exercício",
                example = "https://cdn.exemplo.com/exercises/supino.png"
        )
        String thumbnailUrl
) {}
