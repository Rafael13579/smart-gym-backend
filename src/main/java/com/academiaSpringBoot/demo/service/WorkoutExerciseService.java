package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import com.academiaSpringBoot.demo.responseDTO.WorkoutExerciseResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutExerciseService(WorkoutExerciseRepository workoutExerciseRepository, WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository) {
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public void addExerciseToWorkout(Long workoutId, Long exerciseId, User user) {

        Workout workout = workoutRepository.findByUserAndId(user, workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(workout);
        we.setExercise(exercise);

        workoutExerciseRepository.save(we);
    }

    @Transactional
    public void deleteWorkoutExercise(Long workoutId, Long weId, User user) {

        Workout workout = workoutRepository.findByUserAndId(user, workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        WorkoutExercise we = workout.getWorkoutExercises().stream()
                .filter(e -> e.getId().equals(weId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout exercise not found"));

        workout.getWorkoutExercises().remove(we);
    }


    public List<WorkoutExerciseResponseDTO> listByWorkout(Long workoutId, User user) {
        Workout workout = workoutRepository.findByUserAndId(user, workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        List<WorkoutExercise> entities = workoutExerciseRepository.findByWorkout(workout);

        return entities.stream()
                .map(this::mapResponseToDTO)
                .toList();
    }

    public WorkoutExerciseResponseDTO mapResponseToDTO(WorkoutExercise we) {
        List<TrainingSetResponseDTO> trainingSets = we.getTrainingSets().stream()
                .map(ts -> new TrainingSetResponseDTO(
                        ts.getId(),
                        ts.getWeight(),
                        ts.getReps()))
                .toList();

        return new WorkoutExerciseResponseDTO(we.getId(),
                we.getExercise().getId(),
                we.getExercise().getName(),
                we.getExercise().getMuscularGroup(),
                we.getExercise().getDescription(),
                trainingSets);


        }
    }


