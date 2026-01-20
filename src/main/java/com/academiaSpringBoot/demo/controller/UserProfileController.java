package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.dto.createDTO.UserProfileCreateDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.UserProfileResponseDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.service.UserProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Profile", description = "Gerencia o perfil do usuário")
@RestController
@RequestMapping("/profile")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "Criar perfil do usuário", description = "Cria um perfil para o usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping()
    public ResponseEntity<UserProfileResponseDTO> createUserProfile(
            @RequestBody UserProfileCreateDTO dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userProfileService.create(user, dto));
    }

    @Operation(summary = "Atualizar perfil do usuário", description = "Atualiza o perfil do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @PutMapping()
    public UserProfileResponseDTO update(@RequestBody UserProfileCreateDTO dto, Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return userProfileService.update(user, dto);
    }


    @Operation(summary = "Consultar perfil do usuário", description = "Retorna o perfil do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    @GetMapping
    public UserProfileResponseDTO getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userProfileService.getByUser(user);
    }
}
