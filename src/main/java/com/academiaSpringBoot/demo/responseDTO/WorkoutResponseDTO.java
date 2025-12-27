package com.academiaSpringBoot.demo.responseDTO;

import com.academiaSpringBoot.demo.Model.Exercise;

import java.util.List;

public record WorkoutResponseDTO(Long id,
                                 String name,
                                 List<ExerciseResponseDTO> exercises) {
}
