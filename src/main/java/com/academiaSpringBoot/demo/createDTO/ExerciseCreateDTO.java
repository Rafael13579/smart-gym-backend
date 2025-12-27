package com.academiaSpringBoot.demo.createDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public record ExerciseCreateDTO(@NotBlank String name,
                               @NotBlank String muscularGroup,
                               @NotBlank String description) {}







