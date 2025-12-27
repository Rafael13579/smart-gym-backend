package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.Model.Exercise;
import com.academiaSpringBoot.demo.createDTO.ExerciseCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ExerciseResponseDTO> addExercise(@PathVariable Long workoutId, @RequestBody ExerciseCreateDTO dto) {

        ExerciseResponseDTO exercise = exerciseService.create(workoutId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDTO>> findAllByWorkoutId(@PathVariable Long workoutId) {
        return ResponseEntity.ok(exerciseService.listByWorkout(workoutId));
    }
}

