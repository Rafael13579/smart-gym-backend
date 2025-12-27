package com.academiaSpringBoot.demo.responseDTO;

import com.academiaSpringBoot.demo.Model.Workout;

import java.util.List;

public record UserResponseDTO(Long id,
                              String email,
                              String name,
                              List<WorkoutResponseDTO> workouts) {
}
