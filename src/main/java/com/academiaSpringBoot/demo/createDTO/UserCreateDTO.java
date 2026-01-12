package com.academiaSpringBoot.demo.createDTO;

import com.academiaSpringBoot.demo.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para criação de usuário no sistema")
public record UserCreateDTO(
        @Schema(description = "Nome completo do usuário", example = "Rafael Fernandes")
        @NotBlank(groups = {OnCreate.class, OnUpdate.class})
        String name,

        @Schema(description = "Email do usuário (usado para login)", example = "rafael@email.com")
        @NotBlank(groups = OnCreate.class)
        String email,

        @Schema(description = "Senha do usuário (será criptografada no backend)", example = "123456")
        @NotBlank(groups = OnCreate.class)
        String password,

        @Schema(description = "Perfil do usuário no sistema", example = "USER", allowableValues = {"USER", "ADMIN"})
        User.Role role
) {
        public interface OnCreate {}
        public interface OnUpdate {}
}
