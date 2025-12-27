package com.academiaSpringBoot.demo.responseDTO;

import com.academiaSpringBoot.demo.Model.TrainingSet;

import java.util.List;

public record ExerciseResponseDTO(Long Id,
                                  String name,
                                  String muscularGroup,
                                  List<TrainingSetResponseDTO> trainingSets) {
}
