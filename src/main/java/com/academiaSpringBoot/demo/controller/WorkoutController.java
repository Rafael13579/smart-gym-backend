package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.WeekDays;
import com.academiaSpringBoot.demo.responseDTO.WorkoutResponseDTO;
import com.academiaSpringBoot.demo.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wokrout", description = "Gerenciamento de treino")
@RestController
@RequestMapping("/workouts")
@CrossOrigin
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }


    //CREATE
    @Operation(summary = "Criar treino", description = "Cria um novo para o usuário associado a um dia da semana")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Treino criado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Dados Inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> create(@RequestBody @Validated(WorkoutCreateDTO.OnCreate.class) WorkoutCreateDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(workoutService.create(user, dto));
    }


    //LIST
    @Operation(summary = "Listar treinos", description = "Lista todos os treinos pertencentes a um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutResponseDTO>> list(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(workoutService.listByUser(user));
    }


    //UPDATE
    @Operation(summary = "Atualizar nome do treino", description = "Atualiza o nome de um treino pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseDTO> updateName(@PathVariable Long workoutId, @RequestBody @Validated(WorkoutCreateDTO.OnUpdate.class) WorkoutCreateDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(workoutService.updateWorkoutName(workoutId, user, dto.name()));
    }


    //DELETE
    @Operation(summary = "Remover treino", description = "Remove um treino pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Treino removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> delete(@PathVariable Long workoutId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        workoutService.deleteWorkout(workoutId, user);

        return ResponseEntity.noContent().build();
    }


    //FIND_BY_DAY
    @Operation(summary = "Buscar treino por dia da semana", description = "Retorna o treino do usuário autenticadoassociado a um dia específico da semana. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treino encontrado"),
            @ApiResponse(responseCode = "404", description = "Treino não encontrado para o dia informado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/{day}")
    public ResponseEntity<WorkoutResponseDTO> findByDay(@PathVariable WeekDays day, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(workoutService.listWorkoutByDay(user, day));
    }
}
