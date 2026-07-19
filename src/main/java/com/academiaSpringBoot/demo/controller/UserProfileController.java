package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.dto.createDTO.UserProfileCreateDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.UserProfileResponseDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Profile", description = "Gerencia o perfil do usuario")
@RestController
@RequestMapping("/profile")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "Criar perfil do usuario", description = "Cria um perfil para o usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado")
    })
    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> createUserProfile(
            @RequestBody @Validated(UserProfileCreateDTO.OnCreate.class) UserProfileCreateDTO dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userProfileService.create(user, dto));
    }

    @Operation(summary = "Atualizar perfil do usuario", description = "Atualiza o perfil do usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado"),
            @ApiResponse(responseCode = "404", description = "Perfil nao encontrado")
    })
    @PutMapping
    public UserProfileResponseDTO update(
            @RequestBody @Validated(UserProfileCreateDTO.OnUpdate.class) UserProfileCreateDTO dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return userProfileService.update(user, dto);
    }

    @Operation(summary = "Consultar perfil do usuario", description = "Retorna o perfil do usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado"),
            @ApiResponse(responseCode = "404", description = "Perfil nao encontrado")
    })
    @GetMapping
    public UserProfileResponseDTO getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userProfileService.getByUser(user);
    }
}
