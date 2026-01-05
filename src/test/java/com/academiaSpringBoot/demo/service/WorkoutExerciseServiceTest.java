package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.Exercise;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.Workout;
import com.academiaSpringBoot.demo.model.WorkoutExercise;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutExerciseServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;

    @Test
    void shouldAddWorkoutExerciseSuccessfully() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setId(1L);

        Exercise exercise = new Exercise();
        exercise.setId(1L);

        when(workoutRepository.findById(anyLong()))
                .thenReturn(Optional.of(workout));


        when(exerciseRepository.findById(1L))
                .thenReturn(Optional.of(exercise));

        workoutExerciseService.addExerciseToWorkout(1L, 1L, user);

        verify(workoutExerciseRepository).save(argThat(we ->
                we.getWorkout().equals(workout) &&
                        we.getExercise().equals(exercise)
        ));
    }
}
