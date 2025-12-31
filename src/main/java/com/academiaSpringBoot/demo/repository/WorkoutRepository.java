package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    public List<Workout> findByUser(User user);
}
