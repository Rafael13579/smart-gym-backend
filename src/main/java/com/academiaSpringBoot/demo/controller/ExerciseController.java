package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.ExerciseCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Exercises", description = "Endpoints para gerenciamento e consulta de exercícios")
@RestController
@RequestMapping("/exercises")
@CrossOrigin
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }


    //CREATE
    @Operation(summary = "Criar exercício", description = "Cria um novo exercício no banco de dados (apenas administradores)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createExercise(@RequestBody @Validated(ExerciseCreateDTO.OnCreate.class) ExerciseCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.create(dto));
    }


    //DELETE
    @Operation(summary = "Remover exercício", description = "Remove um exercício existente do sistema (apenas administradores)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercício removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long exerciseId) {
        exerciseService.deleteExercise(exerciseId);

        return ResponseEntity.noContent().build();
    }


    //FIND_ALL
    @Operation(summary = "Listar exercícios", description = "Retorna uma lista paginada de exercícios cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping
    public ResponseEntity<Page<ExerciseResponseDTO>> findAll(@ParameterObject @PageableDefault(size = 5, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(exerciseService.findAll(pageable));
    }


    //AUTOCOMPLETE
    @Operation(summary = "Autocomplete de exercícios", description = "Retorna até 5 exercícios cujo nome começa com o texto informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de exercícios"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/autocomplete")
    public ResponseEntity<List<ExerciseResponseDTO>> autocomplete(
            @Parameter(description = "Texto inicial do nome do exercício (mínimo 3 caracteres)", example = "sup")
            @RequestParam String query) {

        if(query.length() < 3) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(exerciseService.autocompleteExercise(query));
    }

    //UPDATE
    @Operation(summary = "Atualização de atributos de exercíco", description = "Atualiza algum atributo do exercício")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/exerciseId")
    public ResponseEntity<ExerciseResponseDTO> partialExerciseUpdate(@PathVariable Long exerciseId, @RequestBody @Validated(ExerciseCreateDTO.OnUpdate.class) ExerciseCreateDTO dto) {
        return ResponseEntity.ok(exerciseService.partialUpdate(exerciseId, dto));
    }

}


