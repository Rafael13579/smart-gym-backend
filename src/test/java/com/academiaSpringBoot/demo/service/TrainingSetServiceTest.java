/*
package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.TrainingSetRepository;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.TrainingSetResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingSetServiceTest {

    @Mock
    private TrainingSetRepository trainingSetRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @InjectMocks
    private TrainingSetService trainingSetService;

    @Test
    void shouldCreateTrainingSetSuccessfully() {

        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);

        Exercise exercise = new Exercise();
        exercise.setId(1L);

        WorkoutExercise we = new WorkoutExercise();
        we.setId(1L);
        we.setWorkout(workout);
        we.setExercise(exercise);

        TrainingSetCreateDTO dto = new TrainingSetCreateDTO(20.0, 12);


        when(workoutRepository.findById(1L))
                .thenReturn(Optional.of(workout));

        when(exerciseRepository.findById(1L))
                .thenReturn(Optional.of(exercise));

        when(workoutExerciseRepository
                .findByWorkoutAndExercise(workout, exercise))
                .thenReturn(Optional.of(we));

        when(trainingSetRepository.save(any(TrainingSet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // execução
        trainingSetService.create(1L, 1L, dto, user);

        // verificação
        verify(trainingSetRepository).save(argThat(ts ->
                ts.getWeight() == 20 &&
                        ts.getReps() == 12 &&
                        ts.getWorkoutExercise().equals(we)
        ));
    }
}
*/