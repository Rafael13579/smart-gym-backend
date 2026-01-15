package com.academiaSpringBoot.demo.controller;


import com.academiaSpringBoot.demo.dto.gemini.AiGenerationRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiReplaceWorkoutPlanDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.service.AiWorkoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiWorkoutService aiWorkoutService;

    public AiController(AiWorkoutService aiWorkoutService) {
        this.aiWorkoutService = aiWorkoutService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generateWorkout(@RequestBody AiGenerationRequestDTO request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        aiWorkoutService.generateWorkoutForUser(user, request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/replace")
    public ResponseEntity<Void> replaceWorkout(@RequestBody AiReplaceWorkoutPlanDTO replace, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        aiWorkoutService.replaceWorkoutPlan(user, replace);

        return ResponseEntity.ok().build();
    }
}