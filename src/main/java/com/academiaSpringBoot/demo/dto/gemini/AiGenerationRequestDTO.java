package com.academiaSpringBoot.demo.dto.gemini;

import com.academiaSpringBoot.demo.model.WeekDays;
import java.util.List;

public record AiGenerationRequestDTO(
        String goal,               // ex: Hipertrofia
        String experienceLevel,    // ex: Intermediário
        Integer durationInMinutes, // ex: 60
        List<WeekDays> availableDays// ex: ["MONDAY", "WEDNESDAY", "FRIDAY"]
) {}