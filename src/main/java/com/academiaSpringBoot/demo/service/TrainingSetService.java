package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.Exercise;
import com.academiaSpringBoot.demo.model.TrainingSet;
import com.academiaSpringBoot.demo.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.TrainingSetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TrainingSetService {

    private TrainingSetRepository trainingSetRepository;
    private ExerciseRepository exerciseRepository;

    public TrainingSetService(TrainingSetRepository trainingSetRepository, ExerciseRepository exerciseRepository) {
        this.trainingSetRepository = trainingSetRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public TrainingSetResponseDTO create(Long exerciseId, TrainingSetCreateDTO dto){
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        TrainingSet trainingSet = TrainingSet.builder()
                .weight(dto.weight())
                .reps(dto.reps())
                .exercise(exercise)
                .build();

        TrainingSet saved = trainingSetRepository.save(trainingSet);

        return new TrainingSetResponseDTO(
                saved.getId(),
                saved.getWeight(),
                saved.getReps()
        );

    }

    public List<TrainingSetResponseDTO> findByExerciseId(Long exerciseId) {
        return trainingSetRepository.getTrainingSetByExerciseId(exerciseId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public TrainingSetResponseDTO mapToResponseDTO(TrainingSet trainingSet) {
        return new TrainingSetResponseDTO(
                trainingSet.getId(),
                trainingSet.getWeight(),
                trainingSet.getReps()
        );
    }
}
