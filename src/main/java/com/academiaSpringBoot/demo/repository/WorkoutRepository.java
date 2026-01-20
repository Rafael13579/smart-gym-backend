package com.academiaSpringBoot.demo.repository;

import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.WeekDays;
import com.academiaSpringBoot.demo.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUser(User user);

    boolean existsByUserAndDay(User user, WeekDays days);

    Optional<Workout> findByUserAndId(User user, Long workoutId);

    Optional<Workout> findByUserAndDay(User user, WeekDays day);

    @Modifying
    @Transactional
    void deleteByUser(User user);
}
