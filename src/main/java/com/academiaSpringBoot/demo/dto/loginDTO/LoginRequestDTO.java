package com.academiaSpringBoot.demo.dto.loginDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para login do usuário")
public record LoginRequestDTO(

        @Schema(
                description = "Senha do usuário",
                example = "123456"
        )
        @NotBlank
        String password,

        @Schema(
                description = "Email do usuário",
                example = "rafael@email.com"
        )
        @Email
        @NotBlank
        String email
) {}
