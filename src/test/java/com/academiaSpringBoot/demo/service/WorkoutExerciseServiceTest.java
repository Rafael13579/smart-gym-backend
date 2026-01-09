package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.WorkoutExerciseResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutExerciseServiceTest {

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private WorkoutExerciseService workoutExerciseService;


    @Test
    void shouldAddExerciseToWorkoutSuccessfully() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setId(10L);

        Exercise exercise = new Exercise();
        exercise.setId(100L);

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        when(exerciseRepository.findById(100L))
                .thenReturn(Optional.of(exercise));

        when(workoutExerciseRepository.save(any(WorkoutExercise.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        workoutExerciseService.addExerciseToWorkout(10L, 100L, user);

        verify(workoutExerciseRepository, times(1))
                .save(any(WorkoutExercise.class));
    }

    @Test
    void shouldThrowExceptionWhenWorkoutDoesNotExist() {
        User user = new User();

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                workoutExerciseService.addExerciseToWorkout(10L, 100L, user)
        );

        assertEquals("404 NOT_FOUND \"Workout not found\"", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenExerciseDoesNotExist() {
        User user = new User();
        Workout workout = new Workout();
        workout.setId(10L);

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        when(exerciseRepository.findById(100L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                workoutExerciseService.addExerciseToWorkout(10L, 100L, user)
        );

        assertEquals("404 NOT_FOUND \"Exercise not found\"", ex.getMessage());
    }


    @Test
    void shouldDeleteWorkoutExerciseSuccessfully() {
        User user = new User();
        Workout workout = new Workout();
        workout.setId(10L);

        WorkoutExercise we = new WorkoutExercise();
        we.setId(100L);

        workout.setWorkoutExercises(new java.util.ArrayList<>(List.of(we)));

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        workoutExerciseService.deleteWorkoutExercise(10L, 100L, user);

        assertTrue(workout.getWorkoutExercises().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenWorkoutDoesNotExistOnDelete() {
        User user = new User();

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                workoutExerciseService.deleteWorkoutExercise(10L, 100L, user)
        );

        assertEquals("404 NOT_FOUND \"Workout not found\"", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkoutExerciseDoesNotExist() {
        User user = new User();
        Workout workout = new Workout();
        workout.setId(10L);
        workout.setWorkoutExercises(List.of());

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                workoutExerciseService.deleteWorkoutExercise(10L, 100L, user)
        );

        assertEquals("404 NOT_FOUND \"Workout exercise not found\"", ex.getMessage());
    }

    @Test
    void shouldListWorkoutExercisesSuccessfully() {
        User user = new User();
        Workout workout = new Workout();
        workout.setId(10L);

        Exercise exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Bench Press");
        exercise.setDescription("Chest exercise");
        exercise.setMuscularGroup("Chest");

        WorkoutExercise we = new WorkoutExercise();
        we.setId(100L);
        we.setExercise(exercise);
        we.setTrainingSets(List.of());

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        when(workoutExerciseRepository.findByWorkout(workout))
                .thenReturn(List.of(we));

        List<WorkoutExerciseResponseDTO> result =
                workoutExerciseService.listByWorkout(10L, user);

        assertEquals(1, result.size());
        assertEquals("Bench Press", result.getFirst().exerciseName());
    }

    @Test
    void shouldThrowExceptionWhenWorkoutDoesNotExistOnList() {
        User user = new User();

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                workoutExerciseService.listByWorkout(10L, user)
        );

        assertEquals("404 NOT_FOUND \"Workout not found\"", ex.getMessage());
    }

    @Test
    void shouldMapWorkoutExerciseToDTOCorrectly() {
        Exercise exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Bench Press");
        exercise.setDescription("Chest exercise");
        exercise.setMuscularGroup("Chest");

        TrainingSet ts = new TrainingSet();
        ts.setId(10L);
        ts.setWeight(50.0);
        ts.setReps(10);

        WorkoutExercise we = new WorkoutExercise();
        we.setId(100L);
        we.setExercise(exercise);
        we.setTrainingSets(List.of(ts));

        WorkoutExerciseResponseDTO dto = workoutExerciseService.mapResponseToDTO(we);

        assertEquals(100L, dto.id());
        assertEquals("Bench Press", dto.exerciseName());
        assertEquals(1, dto.sets().size());
        assertEquals(50.0, dto.sets().getFirst().weight());
    }
}
