package com.academiaSpringBoot.demo.dto.createDTO;

import com.academiaSpringBoot.demo.model.ExperienceLevel;
import com.academiaSpringBoot.demo.model.Goal;
import com.academiaSpringBoot.demo.model.UserSex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para criacao ou atualizacao do perfil do usuario")
public record UserProfileCreateDTO(
        @Schema(description = "Peso do usuario", example = "64.5")
        @NotNull(groups = OnCreate.class, message = "Weight is required")
        @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Weight must be greater than zero")
        Double weight,

        @Schema(description = "Altura do usuario em cm", example = "170")
        @NotNull(groups = OnCreate.class, message = "Height is required")
        @Positive(groups = {OnCreate.class, OnUpdate.class}, message = "Height must be greater than zero")
        Double height,

        @Schema(description = "Idade do usuario", example = "20")
        @NotNull(groups = OnCreate.class, message = "Age is required")
        @Min(value = 1, groups = {OnCreate.class, OnUpdate.class}, message = "Age must be at least 1")
        @Max(value = 120, groups = {OnCreate.class, OnUpdate.class}, message = "Age must be at most 120")
        Integer age,

        @Schema(description = "Sexo do usuario", example = "MALE")
        @NotNull(groups = OnCreate.class, message = "Sex is required")
        UserSex sex,

        @Schema(description = "Objetivo do usuario", example = "HYPERTROPHY")
        @NotNull(groups = OnCreate.class, message = "Goal is required")
        Goal goal,

        @Schema(description = "Nivel de experiencia do usuario", example = "INTERMEDIATE")
        @NotNull(groups = OnCreate.class, message = "Experience level is required")
        ExperienceLevel experienceLevel
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
