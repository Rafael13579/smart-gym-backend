package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.dto.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.*;
import com.academiaSpringBoot.demo.service.TrainingSetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class TrainingSetIntegrationTest {

    @Autowired
    private TrainingSetService trainingSetService;

    @Autowired private UserRepository userRepository;
    @Autowired private WorkoutRepository workoutRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private WorkoutExerciseRepository workoutExerciseRepository;
    @Autowired private TrainingSetRepository trainingSetRepository;

    private User standardUser;
    private Workout standardWorkout;
    private Exercise standardExercise;
    private WorkoutExercise standardWe;

    @BeforeEach
    void setup() {
        standardUser = User.builder()
                .name("Arnold")
                .email("arnold@gym.com")
                .password("123")
                .role(User.Role.USER)
                .build();
        userRepository.save(standardUser);

        standardWorkout = Workout.builder()
                .name("Treino Costas")
                .day(WeekDays.FRIDAY)
                .user(standardUser)
                .workoutExercises(new ArrayList<>())
                .build();
        workoutRepository.save(standardWorkout);

        standardExercise = new Exercise();
        standardExercise.setName("Puxada Alta");
        standardExercise.setMuscularGroup("Costas");
        standardExercise.setDescription("...");
        exerciseRepository.save(standardExercise);

        standardWe = new WorkoutExercise();
        standardWe.setWorkout(standardWorkout);
        standardWe.setExercise(standardExercise);
        standardWe.setTrainingSets(new ArrayList<>());
        workoutExerciseRepository.save(standardWe);
    }

    @Test
    void shouldCreateTrainingSet() {
        TrainingSetCreateDTO dto = new TrainingSetCreateDTO(50.0, 12);

        var response = trainingSetService.create(standardWorkout.getId(), standardExercise.getId(), dto, standardUser);

        assertNotNull(response.id());
        assertEquals(50.0, response.weight());
        assertEquals(12, response.reps());

        assertEquals(1, trainingSetRepository.count());
    }

    @Test
    void shouldPartialUpdate_ChangeOnlyWeight() {
        TrainingSet set = new TrainingSet(null, 40.0, 10, standardWe);
        trainingSetRepository.save(set);

        TrainingSetCreateDTO updateDto = new TrainingSetCreateDTO(60.0, null);

        var response = trainingSetService.partialUpdate(set.getId(), updateDto, standardUser);

        assertEquals(60.0, response.weight());
        assertEquals(10, response.reps());
    }

    @Test
    void shouldDeleteSet() {
        TrainingSet set = new TrainingSet(null, 100.0, 5, standardWe);
        trainingSetRepository.save(set);

        trainingSetService.deleteSet(standardWe.getId(), set.getId(), standardUser);

        assertFalse(trainingSetRepository.findById(set.getId()).isPresent());
    }

    @Test
    void shouldThrowException_WhenCreatingSetForAnotherUser() {
        User invasor = User.builder().name("Invasor").email("inv@test.com").password("123").role(User.Role.USER).build();
        userRepository.save(invasor);

        TrainingSetCreateDTO dto = new TrainingSetCreateDTO(10.0, 10);

        assertThrows(ResponseStatusException.class, () -> {
            trainingSetService.create(standardWorkout.getId(), standardExercise.getId(), dto, invasor);
        });
    }

    @Test
    void shouldThrowException_WhenUpdatingSetOfAnotherUser() {
        TrainingSet set = new TrainingSet(null, 40.0, 10, standardWe);
        trainingSetRepository.save(set);

        User invasor = User.builder().name("Invasor").email("inv@test.com").password("123").role(User.Role.USER).build();
        userRepository.save(invasor);

        TrainingSetCreateDTO dto = new TrainingSetCreateDTO(50.0, null);

        assertThrows(ResponseStatusException.class, () -> {
            trainingSetService.partialUpdate(set.getId(), dto, invasor);
        });
    }

    @Test
    void shouldThrowException_WhenDeletingSetWithWrongWorkoutExerciseId() {
        TrainingSet set = new TrainingSet(null, 100.0, 5, standardWe);
        trainingSetRepository.save(set);

        Long idErradoDeWorkoutExercise = 9999L;

        assertThrows(ResponseStatusException.class, () -> {
            trainingSetService.deleteSet(idErradoDeWorkoutExercise, set.getId(), standardUser);
        });
    }
}