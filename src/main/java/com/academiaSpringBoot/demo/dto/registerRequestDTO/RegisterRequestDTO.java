package com.academiaSpringBoot.demo.dto.registerRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para registro público de usuários")
public record RegisterRequestDTO(

        @Schema(description = "Nome completo do usuário", example = "Rafael Fernandes")
        @NotBlank
        String name,

        @Schema(description = "Email do usuário (usado para login)", example = "rafael@email.com")
        @Email
        @NotBlank
        String email,

        @Schema(description = "Senha do usuário (mínimo de 6 caracteres)", example = "123456")
        @Size(min = 6)
        String password
) {}
