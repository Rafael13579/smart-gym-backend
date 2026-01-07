package com.academiaSpringBoot.demo.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Modelo padrão de erro retornado pela API")
public class ApiError {

    @Schema(
            description = "Código HTTP do erro",
            example = "404"
    )
    private int status;

    @Schema(
            description = "Tipo do erro",
            example = "Resource not found"
    )
    private String error;

    @Schema(
            description = "Mensagem detalhada do erro",
            example = "Exercise not found"
    )
    private String message;

    @Schema(
            description = "Caminho da requisição que gerou o erro",
            example = "/exercises/10"
    )
    private String path;

    @Schema(
            description = "Data e hora em que o erro ocorreu",
            example = "2026-01-05T22:23:04.233"
    )
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
