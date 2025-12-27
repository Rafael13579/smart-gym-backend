package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.Model.Workout;
import com.academiaSpringBoot.demo.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.WorkoutResponseDTO;
import com.academiaSpringBoot.demo.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@CrossOrigin
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public WorkoutResponseDTO create(@RequestBody @Valid WorkoutCreateDTO dto) {
        return workoutService.create(dto);

    }

    @GetMapping
    public List<WorkoutResponseDTO> list(@RequestParam Long userId) {
        return workoutService.listByUser(userId);
    }
}
