package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.Model.Exercise;
import com.academiaSpringBoot.demo.Model.TrainingSet;
import com.academiaSpringBoot.demo.Model.Workout;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.academiaSpringBoot.demo.createDTO.ExerciseCreateDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    public ExerciseResponseDTO create(Long workoutId, ExerciseCreateDTO dto) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        Exercise exercise = Exercise.builder()
                .name(dto.name())
                .muscularGroup(dto.muscularGroup())
                .description(dto.description())
                .workout(workout)
                .build();

        Exercise saved = exerciseRepository.save(exercise);

        return new ExerciseResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getMuscularGroup(),
                List.of()
        );
    }

    public List<ExerciseResponseDTO> listByWorkout(Long workoutId) {
        return exerciseRepository.findByWorkoutId(workoutId)
                .stream()
                .map(this::mapResponseToDTO)
                .toList();
    }


    public ExerciseResponseDTO mapResponseToDTO(Exercise exercise) {
        List<TrainingSetResponseDTO> sets = List.of();

        if(exercise.getSets() != null) {
            sets = exercise.getSets()
                    .stream()
                    .map(ts -> new TrainingSetResponseDTO(
                            ts.getId(),
                            ts.getWeight(),
                            ts.getReps()
                    ))
                    .toList();

        }

        return new ExerciseResponseDTO(
                exercise.getId(),
                exercise.getName(),
                exercise.getMuscularGroup(),
                sets
        );
    }
}
