package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.dto.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.TrainingSetRepository;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.dto.responseDTO.TrainingSetResponseDTO;
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
class TrainingSetServiceTest {

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private TrainingSetRepository trainingSetRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private TrainingSetService trainingSetService;


    @Test
    void shouldCreateTrainingSetSuccessfully() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setId(10L);
        workout.setUser(user);

        Exercise exercise = new Exercise();
        exercise.setId(100L);

        WorkoutExercise we = new WorkoutExercise();
        we.setId(200L);
        we.setWorkout(workout);
        we.setExercise(exercise);

        TrainingSetCreateDTO dto = new TrainingSetCreateDTO(50.0, 10);

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        when(exerciseRepository.findById(100L))
                .thenReturn(Optional.of(exercise));

        when(workoutExerciseRepository.findByWorkoutAndExercise(workout, exercise))
                .thenReturn(Optional.of(we));

        when(trainingSetRepository.save(any(TrainingSet.class)))
                .thenAnswer(inv -> {
                    TrainingSet ts = inv.getArgument(0);
                    ts.setId(1L);
                    return ts;
                });

        TrainingSetResponseDTO response =
                trainingSetService.create(10L, 100L, dto, user);

        assertNotNull(response);
        assertEquals(50.0, response.weight());
        assertEquals(10, response.reps());

        verify(trainingSetRepository, times(1)).save(any(TrainingSet.class));
    }

    @Test
    void shouldThrowExceptionWhenWorkoutNotFoundOnCreate() {
        User user = new User();

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.create(10L, 100L, new TrainingSetCreateDTO(50.0, 10), user)
        );

        assertEquals("404 NOT_FOUND \"Workout not found\"", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenExerciseNotFoundOnCreate() {
        User user = new User();
        Workout workout = new Workout();

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        when(exerciseRepository.findById(100L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.create(10L, 100L, new TrainingSetCreateDTO(50.0, 10), user)
        );

        assertEquals("404 NOT_FOUND \"Exercise not found\"", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenWorkoutExerciseNotFoundOnCreate() {
        User user = new User();
        Workout workout = new Workout();
        Exercise exercise = new Exercise();

        when(workoutRepository.findByUserAndId(user, 10L))
                .thenReturn(Optional.of(workout));

        when(exerciseRepository.findById(100L))
                .thenReturn(Optional.of(exercise));

        when(workoutExerciseRepository.findByWorkoutAndExercise(workout, exercise))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.create(10L, 100L, new TrainingSetCreateDTO(50.0, 10), user)
        );

        assertEquals("404 NOT_FOUND \"WorkoutExercise not found\"", ex.getMessage());
    }


    @Test
    void shouldDeleteTrainingSetSuccessfully() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setUser(user);

        WorkoutExercise we = new WorkoutExercise();
        we.setId(200L);
        we.setWorkout(workout);

        TrainingSet set = new TrainingSet();
        set.setId(1L);
        set.setWorkoutExercise(we);

        when(trainingSetRepository.findById(1L))
                .thenReturn(Optional.of(set));

        trainingSetService.deleteSet(200L, 1L, user);

        verify(trainingSetRepository, times(1)).delete(set);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNotAllowed() {
        User user = new User();
        user.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Workout workout = new Workout();
        workout.setUser(otherUser);

        WorkoutExercise we = new WorkoutExercise();
        we.setId(200L);
        we.setWorkout(workout);

        TrainingSet set = new TrainingSet();
        set.setWorkoutExercise(we);

        when(trainingSetRepository.findById(1L))
                .thenReturn(Optional.of(set));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.deleteSet(200L, 1L, user)
        );

        assertEquals("400 BAD_REQUEST \"Not allowed operation\"", ex.getMessage());
    }


    @Test
    void shouldPartiallyUpdateTrainingSetSuccessfully() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setUser(user);

        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(workout);

        TrainingSet set = new TrainingSet();
        set.setId(1L);
        set.setWeight(40.0);
        set.setReps(8);
        set.setWorkoutExercise(we);

        when(trainingSetRepository.findById(1L))
                .thenReturn(Optional.of(set));

        TrainingSetCreateDTO dto = new TrainingSetCreateDTO(50.0, 10);

        TrainingSetResponseDTO response =
                trainingSetService.partialUpdate(1L, dto, user);

        assertEquals(50.0, response.weight());
        assertEquals(10, response.reps());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOnUpdate() {
        User user = new User();
        user.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Workout workout = new Workout();
        workout.setUser(otherUser);

        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(workout);

        TrainingSet set = new TrainingSet();
        set.setWorkoutExercise(we);

        when(trainingSetRepository.findById(1L))
                .thenReturn(Optional.of(set));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.partialUpdate(1L, new TrainingSetCreateDTO(50.0, 10), user)
        );

        assertEquals("403 FORBIDDEN \"Access denied\"", ex.getMessage());
    }


    @Test
    void shouldListTrainingSetsSuccessfully() {
        User user = new User();
        user.setId(1L);

        Workout workout = new Workout();
        workout.setUser(user);

        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(workout);

        TrainingSet set = new TrainingSet();
        set.setId(1L);
        set.setWeight(50.0);
        set.setReps(10);

        we.setTrainingSets(List.of(set));

        when(workoutExerciseRepository.findByIdWithSets(10L))
                .thenReturn(Optional.of(we));

        List<TrainingSetResponseDTO> result =
                trainingSetService.listByWorkoutExercise(10L, user);

        assertEquals(1, result.size());
        assertEquals(50.0, result.getFirst().weight());
    }

    @Test
    void shouldThrowExceptionWhenWorkoutExerciseNotFoundOnList() {
        when(workoutExerciseRepository.findByIdWithSets(10L))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.listByWorkoutExercise(10L, new User())
        );

        assertEquals("404 NOT_FOUND \"WorkoutExercise not found\"", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOnList() {
        User user = new User();
        user.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Workout workout = new Workout();
        workout.setUser(otherUser);

        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(workout);

        when(workoutExerciseRepository.findByIdWithSets(10L))
                .thenReturn(Optional.of(we));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                trainingSetService.listByWorkoutExercise(10L, user)
        );

        assertEquals("403 FORBIDDEN", ex.getMessage());
    }
}
