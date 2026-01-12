package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.TrainingSetRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TrainingSetService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final TrainingSetRepository trainingSetRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    public TrainingSetService(TrainingSetRepository trainingSetRepository, WorkoutRepository workoutRepository,  WorkoutExerciseRepository workoutExerciseRepository,  ExerciseRepository exerciseRepository) {
        this.trainingSetRepository = trainingSetRepository;
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public TrainingSetResponseDTO create(Long workoutId, Long exerciseId, TrainingSetCreateDTO dto, User user){
        Workout workout = workoutRepository.findByUserAndId(user, workoutId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout not found"));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found"));

        WorkoutExercise we = workoutExerciseRepository.findByWorkoutAndExercise(workout, exercise)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutExercise not found"));


        TrainingSet set = TrainingSet.builder()
                .weight(dto.weight())
                .reps(dto.reps())
                .workoutExercise(we)
                .build();


        TrainingSet saved = trainingSetRepository.save(set);

        return new TrainingSetResponseDTO(
                saved.getId(),
                saved.getWeight(),
                saved.getReps()
        );

    }

    @Transactional
    public void deleteSet(Long workoutExerciseId, Long trainingSetId, User user){
        TrainingSet set = trainingSetRepository.findById(trainingSetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TrainingSet not found"));

        if(!set.getWorkoutExercise().getId().equals(workoutExerciseId) || !set.getWorkoutExercise().getWorkout().getUser().equals(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed operation");
        }

        trainingSetRepository.delete(set);
    }


    @Transactional
    public TrainingSetResponseDTO partialUpdate(Long trainingSetId, TrainingSetCreateDTO dto, User user) {
        TrainingSet set = trainingSetRepository.findById(trainingSetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Set not found"));

        if (!set.getWorkoutExercise().getWorkout().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        if (dto.reps() != null) {
            set.setReps(dto.reps());
        }

        if (dto.weight() != null) {
            set.setWeight(dto.weight());
        }

        return mapToResponseDTO(set);
    }

    @Transactional
    public List<TrainingSetResponseDTO> listByWorkoutExercise(Long workoutExerciseId, User user) {

        WorkoutExercise we = workoutExerciseRepository.findByIdWithSets(workoutExerciseId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutExercise not found")
                );

        if (!we.getWorkout().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return we.getTrainingSets()
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
