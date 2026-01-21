package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.dto.gemini.AiGenerationRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiReplaceWorkoutPlanDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.service.AiWorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI", description = "Endpoints para geração e substituição de treinos usando IA")
@RestController
@RequestMapping("/ai")
public class AiWorkoutController {

    private final AiWorkoutService aiWorkoutService;

    public AiWorkoutController(AiWorkoutService aiWorkoutService) {
        this.aiWorkoutService = aiWorkoutService;
    }

    @Operation(summary = "Gerar treino com IA", description = "Gera automaticamente um novo plano de treino para o usuário autenticado usando IA.")
    @PostMapping("/generate")
    public ResponseEntity<Void> generateWorkout(@RequestBody AiGenerationRequestDTO request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        aiWorkoutService.generateWorkoutForUser(user, request);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Substituir plano de treino com IA", description = "Substitui o plano de treino atual do usuário autenticado por um novo plano gerado via IA.")
    @PostMapping("/replace")
    public ResponseEntity<Void> replaceWorkout(@RequestBody AiReplaceWorkoutPlanDTO replace, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        aiWorkoutService.replaceWorkoutPlan(user, replace);
        return ResponseEntity.ok().build();
    }
}
