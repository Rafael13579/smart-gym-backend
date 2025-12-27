package com.academiaSpringBoot.demo.createDTO;

import jakarta.validation.constraints.NotBlank;

public record UserCreateDTO(@NotBlank String name,
                            @NotBlank String email,
                            @NotBlank String password) {}
