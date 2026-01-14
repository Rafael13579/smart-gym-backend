package com.academiaSpringBoot.demo.dto.createDTO;

import com.academiaSpringBoot.demo.model.ExperienceLevel;
import com.academiaSpringBoot.demo.model.Goal;
import com.academiaSpringBoot.demo.model.UserSex;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para criação do perfil do usuário")
public record UserProfileCreateDTO(
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
