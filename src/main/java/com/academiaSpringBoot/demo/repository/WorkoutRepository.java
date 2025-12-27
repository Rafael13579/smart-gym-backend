package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.Model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);
}
