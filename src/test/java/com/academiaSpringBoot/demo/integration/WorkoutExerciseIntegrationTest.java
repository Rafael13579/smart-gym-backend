package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.repository.WorkoutExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.service.WorkoutExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class WorkoutExerciseIntegrationTest {

    @Autowired
    private WorkoutExerciseService workoutExerciseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    private User standardUser;
    private Workout standardWorkout;
    private Exercise standardExercise;

    @BeforeEach
    void setup() {
        standardUser = User.builder()
                .name("Aluno Focado")
                .email("focado@test.com")
                .password("123")
                .role(User.Role.USER)
                .build();
        userRepository.save(standardUser);

        standardExercise = new Exercise();
        standardExercise.setName("Supino Reto");
        standardExercise.setMuscularGroup("Peito");
        standardExercise.setDescription("Barra no peito");
        exerciseRepository.save(standardExercise);

        standardWorkout = Workout.builder()
                .name("Treino A")
                .day(WeekDays.MONDAY)
                .user(standardUser)
                .workoutExercises(new ArrayList<>())
                .build();
        workoutRepository.save(standardWorkout);
    }

    @Test
    void shouldAddExerciseToWorkout() {
        workoutExerciseService.addExerciseToWorkout(standardWorkout.getId(), standardExercise.getId(), standardUser);

        // Validação
        List<WorkoutExercise> relations = workoutExerciseRepository.findByWorkout(standardWorkout);
        assertEquals(1, relations.size());
        assertEquals("Supino Reto", relations.getFirst().getExercise().getName());
    }

    @Test
    void shouldListExercisesByWorkout() {
        workoutExerciseService.addExerciseToWorkout(standardWorkout.getId(), standardExercise.getId(), standardUser);

        var resultList = workoutExerciseService.listByWorkout(standardWorkout.getId(), standardUser);

        assertFalse(resultList.isEmpty());
        assertEquals(standardExercise.getId(), resultList.getFirst().exerciseId());
        assertEquals("Supino Reto", resultList.getFirst().exerciseName());
    }

    @Test
    void shouldDeleteWorkoutExercise() {
        WorkoutExercise we = new WorkoutExercise();
        we.setWorkout(standardWorkout);
        we.setExercise(standardExercise);
        we.setTrainingSets(new ArrayList<>());
        workoutExerciseRepository.save(we);

        standardWorkout.getWorkoutExercises().add(we);
        workoutRepository.save(standardWorkout);

        workoutExerciseService.deleteWorkoutExercise(standardWorkout.getId(), we.getId(), standardUser);

        assertFalse(workoutExerciseRepository.findById(we.getId()).isPresent());
    }

    @Test
    void shouldThrowException_WhenAddingExerciseToWrongUserWorkout() {
        User invasor = User.builder().name("Invasor").email("inv@test.com").password("123").role(User.Role.USER).build();
        userRepository.save(invasor);

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutExerciseService.addExerciseToWorkout(standardWorkout.getId(), standardExercise.getId(), invasor);
        });
    }

    @Test
    void shouldThrowException_WhenExerciseNotFound() {
        Long invalidExerciseId = 9999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutExerciseService.addExerciseToWorkout(standardWorkout.getId(), invalidExerciseId, standardUser);
        });
    }
}