package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.UserCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.UserResponseDTO;
import com.academiaSpringBoot.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Endpoints para gerenciamento de usuários")
@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    //CREATE
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existente")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(userService.createUser(dto));
    }


    //LIST
    @Operation(summary = "Listar usuários", description = "Retorna uma lista com todos os usuários cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public List<UserResponseDTO> listAll() {
        return userService.listAllUsers();
    }


    //GET_BY_ID
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário a partir do seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
