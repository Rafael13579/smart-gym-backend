package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.exception.BusinessException;
import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.WeekDays;
import com.academiaSpringBoot.demo.model.Workout;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.WorkoutResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Transactional
    public WorkoutResponseDTO create(User user, WorkoutCreateDTO dto) {
        boolean exist = workoutRepository.existsByUserAndDay(user, dto.day());

        if(exist){
            throw new BusinessException("Workout already exists at this day");
        }

        Workout workout = Workout.builder()
                .name(dto.name())
                .day(dto.day())
                .user(user)
                .build();

        Workout saved = workoutRepository.save(workout);
        return mapResponseToDTO(saved);
    }


    public List<WorkoutResponseDTO> listByUser(User user) {
        return workoutRepository.findByUser(user)
                .stream()
                .map(this::mapResponseToDTO)
                .toList();
    }

    @Transactional
    public WorkoutResponseDTO updateWorkoutName(Long workoutId, User user, String name) {
        Workout workout = workoutRepository.findByUserAndId(user, workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        workout.setName(name);
        return mapResponseToDTO(workout);
    }

    public void deleteWorkout(Long workoutId, User user) {
        Workout workout = workoutRepository.findByUserAndId(user, workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        workoutRepository.delete(workout);
    }

    public WorkoutResponseDTO listWorkoutByDay(User user, WeekDays day) {
        Workout workout = workoutRepository.findByUserAndDay(user, day)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        return mapResponseToDTO(workout);
    }

    public WorkoutResponseDTO mapResponseToDTO(Workout workout) {

       /* List<WorkoutExerciseResponseDTO> we = workout.getWorkoutExercises().stream()
                .map(this::mapResponseToWDTO)
                .toList();
        */
        return new WorkoutResponseDTO(
                workout.getId(),
                workout.getName(),
                workout.getDay(),
                List.of()
        );
    }

    /*
    public WorkoutExerciseResponseDTO mapResponseToWDTO(WorkoutExercise we) {
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
*/
}

