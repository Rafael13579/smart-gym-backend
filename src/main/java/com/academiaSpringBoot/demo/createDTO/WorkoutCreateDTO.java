package com.academiaSpringBoot.demo.createDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public record WorkoutCreateDTO(@NotBlank String name,
                               @NotNull Long userId) {}

