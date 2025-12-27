package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.Model.TrainingSet;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingSetRepository extends JpaRepository<TrainingSet, Long> {
    public List<TrainingSet> getTrainingSetByExerciseId(Long exerciseId);
}
