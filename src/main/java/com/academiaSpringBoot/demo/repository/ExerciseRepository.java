package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByWorkoutId(Long workoutId);
}
