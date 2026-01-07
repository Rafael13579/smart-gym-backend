package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;



public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findById(Long id);

    Page<Exercise> findAll(Pageable pageable);

    Page<Exercise> findByNameStartingWithIgnoreCase(String name, Pageable pageable);
}
