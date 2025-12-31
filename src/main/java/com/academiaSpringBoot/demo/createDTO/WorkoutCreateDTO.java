package com.academiaSpringBoot.demo.createDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkoutCreateDTO(@NotBlank String name) {}

