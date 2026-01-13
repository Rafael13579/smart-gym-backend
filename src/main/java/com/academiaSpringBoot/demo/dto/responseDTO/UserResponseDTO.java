package com.academiaSpringBoot.demo.dto.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO de resposta representando um usuário do sistema")
public record UserResponseDTO(

        @Schema(
                description = "Identificador único do usuário",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Email do usuário",
                example = "usuario@email.com"
        )
        String email,

        @Schema(
                description = "Nome do usuário",
                example = "Rafael Fernandes"
        )
        String name,

        @Schema(
                description = "Lista de treinos associados ao usuário"
        )
        List<WorkoutResponseDTO> workouts
) {}
