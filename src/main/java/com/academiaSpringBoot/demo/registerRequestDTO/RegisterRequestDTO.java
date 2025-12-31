package com.academiaSpringBoot.demo.registerRequestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(@NotBlank String name,
                                 @Email @NotBlank String email,
                                 @Size(min = 6)String password) {
}
