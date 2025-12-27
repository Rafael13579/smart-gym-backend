package com.academiaSpringBoot.demo.createDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;


public record TrainingSetCreateDTO (@PositiveOrZero(message = "Weight must be zero or greater") double weight,
                                    @Min(value = 1, message = "Reps must be at least 1") int reps
)
{}
