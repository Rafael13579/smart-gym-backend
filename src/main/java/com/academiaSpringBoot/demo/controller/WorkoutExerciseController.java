package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.dto.responseDTO.WorkoutExerciseResponseDTO;
import com.academiaSpringBoot.demo.service.WorkoutExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Workout Exercises", description = "Gerenciamento de exercícios associados a um treino")
@RestController
@RequestMapping("/workouts/{workoutId}/exercises")
@CrossOrigin
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    public WorkoutExerciseController(WorkoutExerciseService workoutExerciseService) {
        this.workoutExerciseService = workoutExerciseService;
    }


    //CREATE
    @Operation(summary = "Adicionar exercício ao treino", description = "Associa um exercício existente a um treino do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exercício adicionado ao treino com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Treino não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Treino ou exercício não encontrado")
    })
    @PostMapping("/{exerciseId}")
    public ResponseEntity<Void> addExerciseToWorkout(@PathVariable Long workoutId, @PathVariable Long exerciseId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        workoutExerciseService.addExerciseToWorkout(workoutId, exerciseId, user);

        return ResponseEntity.status(201).build();
    }


    //DELETE
    @Operation(summary = "Remover exercício do treino", description = "Remove um exercício previamente associado a um treino do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercício removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Treino não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada")
    })
    @DeleteMapping("/{weId}")
    public ResponseEntity<Void> deleteWorkoutExercise(@PathVariable Long workoutId, @PathVariable Long weId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        workoutExerciseService.deleteWorkoutExercise(workoutId, weId, user);

        return ResponseEntity.noContent().build();
    }


    //FIND_BY_WORKOUT
    @Operation(summary = "Listar exercícios do treino", description = "Retorna todos os exercícios associados a um treino do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Treino não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutExerciseResponseDTO>> findByWorkout(@PathVariable Long workoutId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(workoutExerciseService.listByWorkout(workoutId, user));
    }
}
