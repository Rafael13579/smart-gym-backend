package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.Exercise;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.academiaSpringBoot.demo.createDTO.ExerciseCreateDTO;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public ExerciseResponseDTO create(ExerciseCreateDTO dto) {

        Exercise exercise = Exercise.builder()
                .name(dto.name())
                .muscularGroup(dto.muscularGroup())
                .description(dto.description())
                .build();

        Exercise saved = exerciseRepository.save(exercise);

        return new ExerciseResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getMuscularGroup(),
                saved.getDescription()
        );
    }

    public List<ExerciseResponseDTO> listAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(this::mapResponseToDTO)
                .toList();
    }

    @Transactional
    public void deleteExercise(Long  exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        exerciseRepository.delete(exercise);
    }


    public Page<ExerciseResponseDTO> findAll(Pageable pageable) {
        return exerciseRepository.findAll(pageable)
                .map(this::mapResponseToDTO);
    }



    public ExerciseResponseDTO mapResponseToDTO(Exercise exercise) {

        return new ExerciseResponseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getMuscularGroup(),
                exercise.getDescription()
        );
    }
}
