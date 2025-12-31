package com.academiaSpringBoot.demo.responseDTO;

import java.util.List;

public record WorkoutResponseDTO(Long id,
                                 String name,
                                 List<ExerciseResponseDTO> exercises) {
}
