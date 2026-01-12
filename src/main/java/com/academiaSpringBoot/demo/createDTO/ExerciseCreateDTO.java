package com.academiaSpringBoot.demo.createDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para criação de um exercício")
public record ExerciseCreateDTO(

        @Schema(description = "Nome do exercício", example = "Supino reto")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Schema(description = "Grupo muscular trabalhado", example = "Peito")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        String muscularGroup,

        @Schema(description = "Descrição do exercício", example = "Exercício para fortalecimento do peitoral")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        String description

) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
