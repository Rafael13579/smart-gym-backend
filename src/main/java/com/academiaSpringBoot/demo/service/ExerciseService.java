package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.Exercise;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.academiaSpringBoot.demo.createDTO.ExerciseCreateDTO;
import java.util.List;


@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
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
                saved.getDescription(),
                saved.getImageUrl()
        );
    }

    @Transactional
    public void deleteExercise(Long  exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        exerciseRepository.delete(exercise);
    }

    @Transactional
    public Page<ExerciseResponseDTO> findAll(Pageable pageable) {
        return exerciseRepository.findAll(pageable)
                .map(this::mapResponseToDTO);
    }

    public List<ExerciseResponseDTO> autocompleteExercise(String name) {
        Pageable limit = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name"));

        return exerciseRepository.findByNameStartingWithIgnoreCase(name.trim(), limit)
                .map(this::mapResponseToDTO)
                .getContent();
    }

    @Transactional
    public ExerciseResponseDTO partialUpdate(Long exerciseId, ExerciseCreateDTO dto) {
        Exercise ex = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        if(dto.name() != null) {
            ex.setName(dto.name());
        }

        if(dto.muscularGroup() != null) {
            ex.setMuscularGroup(dto.muscularGroup());
        }

        if(dto.description() != null) {
            ex.setDescription(dto.description());
        }

        return mapResponseToDTO(ex);
    }

    @Value("${app.upload.base-url}")
    private String baseUrl;

    public ExerciseResponseDTO mapResponseToDTO(Exercise exercise) {
        String finalUrl = (exercise.getImageUrl() != null)
                ? baseUrl + exercise.getImageUrl()
                : baseUrl + "default-exercise.png";

        return new ExerciseResponseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getMuscularGroup(),
                exercise.getDescription(),
                finalUrl
        );
    }
}
