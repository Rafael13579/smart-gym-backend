package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.ExerciseCreateDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts/{workoutId}/exercises")
@CrossOrigin
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> addExercise(@RequestBody @Valid ExerciseCreateDTO dto, Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        ExerciseResponseDTO exercise = exerciseService.create(user, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> findAllByWorkoutId(@PathVariable Long workoutId) {
        return ResponseEntity.ok(exerciseService.listByWorkout(workoutId));
    }
}

