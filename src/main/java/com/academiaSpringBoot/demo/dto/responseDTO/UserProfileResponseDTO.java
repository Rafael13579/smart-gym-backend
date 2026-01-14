package com.academiaSpringBoot.demo.dto.responseDTO;

import com.academiaSpringBoot.demo.model.ExperienceLevel;
import com.academiaSpringBoot.demo.model.Goal;
import com.academiaSpringBoot.demo.model.UserSex;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta representando o perfil de usuário do sistema")
public record UserProfileResponseDTO(
        @Schema(description = "Identificador do perfil do usuário", example = "1")
        Long id,

        @Schema(description = "Peso do usuário", example = "64.5")
        Double weight,

        @Schema(description = "Altura do usuário (em cm)", example = "170")
        Double height,

        @Schema(description = "Idade do usuário", example = "20")
        Integer age,

        @Schema(description = "Sexo do usuário", example = "MALE")
        UserSex sex,

        @Schema(description = "Objetivo do usuário", example = "HYPERTROPHY")
        Goal goal,

        @Schema(description = "Nível de experiência do usuário", example = "INTERMEDIATE")
        ExperienceLevel experienceLevel
) {}
