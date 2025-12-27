package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import com.academiaSpringBoot.demo.service.TrainingSetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("exercise/{exerciseId}/trainingSet")
@CrossOrigin
public class TrainingSetController {

    private final TrainingSetService trainingSetService;

    public TrainingSetController(TrainingSetService trainingSetService){
        this.trainingSetService = trainingSetService;
    }

    @PostMapping
    public ResponseEntity<TrainingSetResponseDTO> createTrainingSet(@PathVariable Long exerciseId, @RequestBody @Valid TrainingSetCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingSetService.create(exerciseId, dto));
    }

    @GetMapping
    public ResponseEntity<List<TrainingSetResponseDTO>> getTrainingSets(@PathVariable Long exerciseId) {
        return ResponseEntity.ok(trainingSetService.findByExerciseId(exerciseId));
    }

}
