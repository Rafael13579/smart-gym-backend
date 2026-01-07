package com.academiaSpringBoot.demo.repository;


import com.academiaSpringBoot.demo.model.TrainingSet;
import com.academiaSpringBoot.demo.model.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TrainingSetRepository extends JpaRepository<TrainingSet, Long> {
    List<TrainingSet> findByWorkoutExercise(WorkoutExercise workoutExercise);

}
