package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.Model.User;
import com.academiaSpringBoot.demo.Model.Workout;
import com.academiaSpringBoot.demo.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.responseDTO.WorkoutResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public WorkoutResponseDTO create(WorkoutCreateDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Workout workout = new Workout();
        workout.setName(dto.name());
        workout.setUser(user);

        Workout saved =workoutRepository.save(workout);

        return new WorkoutResponseDTO(
                workout.getId(),
                workout.getName(),
                List.of()
        );
    }

    public List<WorkoutResponseDTO> listByUser(Long userId) {
        return workoutRepository.findByUserId(userId)
                .stream()
                .map(this::mapResponseToDTO)
                .toList();
    }

    public WorkoutResponseDTO mapResponseToDTO(Workout workout) {
        List<ExerciseResponseDTO> exercises = List.of();

        if(workout.getExercises() != null) {
            exercises = workout.getExercises()
                    .stream()
                    .map(ex -> new ExerciseResponseDTO(
                            ex.getId(),
                            ex.getName(),
                            ex.getMuscularGroup(),
                            List.of()
                        )
                    ).toList();
        }

        return new WorkoutResponseDTO(
                workout.getId(),
                workout.getName(),
                exercises
        );
    }
}
