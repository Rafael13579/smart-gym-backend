package com.academiaSpringBoot.demo.createDTO;

import com.academiaSpringBoot.demo.model.WeekDays;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkoutCreateDTO(@NotBlank String name,
                               @NotNull WeekDays day) {}

