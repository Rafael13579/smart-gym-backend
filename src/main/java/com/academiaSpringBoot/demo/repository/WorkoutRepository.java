package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.WeekDays;
import com.academiaSpringBoot.demo.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUser(User user);

    boolean existsByUserAndDay(User user, WeekDays days);

    Optional<Workout> findByUserAndId(User user, Long workoutId);

    Optional<Workout> findByUserAndDay(User user, WeekDays day);
}
