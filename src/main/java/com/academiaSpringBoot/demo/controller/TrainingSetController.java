package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import com.academiaSpringBoot.demo.service.TrainingSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Training Sets", description = "Gerenciamento de séries de exercícios dentro de um treino")
@RestController
@CrossOrigin
@RequestMapping
public class TrainingSetController {

    private final TrainingSetService trainingSetService;

    public TrainingSetController(TrainingSetService trainingSetService) {
        this.trainingSetService = trainingSetService;
    }


    //CREATE
    @Operation(summary = "Criar série de exercício", description = "Cria uma nova série associada a um exercício dentro de um treino do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Série criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Treino não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Treino ou exercício não encontrado")
    })
    @PostMapping("/workouts/{workoutId}/workout-exercises/{exerciseId}/sets")
    public ResponseEntity<TrainingSetResponseDTO> create(@PathVariable Long exerciseId, @PathVariable Long workoutId, @RequestBody @Valid TrainingSetCreateDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        TrainingSetResponseDTO response = trainingSetService.create(workoutId, exerciseId, dto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    //LIST
    @Operation(summary = "Listar séries de um exercício", description = "Retorna todas as séries associadas a um exercício de um treino do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Treino não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado")
    })
    @GetMapping("/workout-exercises/{workoutExerciseId}/sets")
    public ResponseEntity<List<TrainingSetResponseDTO>> list(@PathVariable Long workoutExerciseId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(trainingSetService.listByWorkoutExercise(workoutExerciseId, user));
    }


    //UPDATE
    @Operation(summary = "Atualizar parcialmente uma série", description = "Atualiza campos específicos de uma série (peso, repetições, etc.)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Série atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Série não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Série não encontrada")
    })
    @PatchMapping("/sets/{trainingSetId}")
    public ResponseEntity<TrainingSetResponseDTO> partialUpdate(@PathVariable Long trainingSetId, @RequestBody TrainingSetCreateDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(trainingSetService.partialUpdate(trainingSetId, dto, user));
    }


    //DELETE
    @Operation(summary = "Excluir série", description = "Remove uma série de um exercício dentro de um treino do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Série removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Série não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Série não encontrada")
    })
    @DeleteMapping("/workout-exercises/{workoutExerciseId}/sets/{trainingSetId}")
    public ResponseEntity<Void> delete(@PathVariable Long workoutExerciseId, @PathVariable Long trainingSetId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        trainingSetService.deleteSet(workoutExerciseId, trainingSetId, user);

        return ResponseEntity.noContent().build();
    }
}

