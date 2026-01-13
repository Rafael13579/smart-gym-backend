package com.academiaSpringBoot.demo.dto.createDTO;

import com.academiaSpringBoot.demo.model.WeekDays;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação ou atualização de um treino")
public record WorkoutCreateDTO(

        @Schema(description = "Nome do treino", example = "Treino A - Peito e Tríceps")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Schema(description = "Dia da semana em que o treino será executado", example = "MONDAY",
                allowableValues = {
                        "MONDAY",
                        "TUESDAY",
                        "WEDNESDAY",
                        "THURSDAY",
                        "FRIDAY",
                        "SATURDAY",
                        "SUNDAY"
                }
        )
        @NotNull(groups = OnCreate.class)
        WeekDays day
) {
        public interface OnUpdate{}
        public interface OnCreate{}
}
