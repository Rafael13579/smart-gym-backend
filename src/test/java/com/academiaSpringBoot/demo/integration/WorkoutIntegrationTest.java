package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.exception.BusinessException;
import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.WeekDays; // Importante: verifique se o import está correto
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class WorkoutIntegrationTest {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    private User standardUser;

    @BeforeEach
    void setup() {
        standardUser = User.builder()
                .name("Aluno Teste")
                .email("aluno@teste.com")
                .password("123456")
                .role(User.Role.USER)
                .build();
        userRepository.save(standardUser);
    }

    @Test
    void shouldCreateWorkout() {
        WorkoutCreateDTO dto = new WorkoutCreateDTO("Treino de Peito", WeekDays.MONDAY);

        var response = workoutService.create(standardUser, dto);

        assertNotNull(response.id());
        assertEquals("Treino de Peito", response.name());
        assertEquals(WeekDays.MONDAY, response.day());
    }

    @Test
    void shouldThrowException_WhenCreatingWorkoutOnSameDay() {
        workoutService.create(standardUser, new WorkoutCreateDTO("Treino A", WeekDays.MONDAY));

        WorkoutCreateDTO dtoDuplicado = new WorkoutCreateDTO("Treino B", WeekDays.MONDAY);

        assertThrows(BusinessException.class, () -> {
            workoutService.create(standardUser, dtoDuplicado);
        });
    }

    @Test
    void shouldListWorkoutsByUser() {
        workoutService.create(standardUser, new WorkoutCreateDTO("Treino A", WeekDays.MONDAY));
        workoutService.create(standardUser, new WorkoutCreateDTO("Treino B", WeekDays.TUESDAY));

        var lista = workoutService.listByUser(standardUser);

        assertEquals(2, lista.size());
    }

    @Test
    void shouldUpdateWorkoutName() {
        WorkoutCreateDTO dto = new WorkoutCreateDTO("Treino Errado", WeekDays.FRIDAY);
        var savedWorkout = workoutService.create(standardUser, dto);

        var updated = workoutService.updateWorkoutName(savedWorkout.id(), standardUser, "Treino Correto");

        assertEquals("Treino Correto", updated.name());

        var noBanco = workoutRepository.findById(savedWorkout.id()).get();
        assertEquals("Treino Correto", noBanco.getName());
    }

    @Test
    void shouldNotUpdateWorkout_IfUserIsNotOwner() {
        var workout = workoutService.create(standardUser, new WorkoutCreateDTO("Treino X", WeekDays.SUNDAY));

        User invasor = User.builder()
                .name("Invasor")
                .email("inv@test.com")
                .password("123")
                .role(User.Role.USER).build();

        userRepository.save(invasor);

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutService.updateWorkoutName(workout.id(), invasor, "Hacked");
        });
    }

    @Test
    void shouldDeleteWorkout() {
        var workout = workoutService.create(standardUser, new WorkoutCreateDTO("Para Deletar", WeekDays.SATURDAY));

        workoutService.deleteWorkout(workout.id(), standardUser);

        assertFalse(workoutRepository.findById(workout.id()).isPresent());
    }

    @Test
    void shouldFindWorkoutByDay() {
        workoutService.create(standardUser, new WorkoutCreateDTO("Treino Pernas", WeekDays.WEDNESDAY));

        var response = workoutService.listWorkoutByDay(standardUser, WeekDays.WEDNESDAY);

        assertEquals("Treino Pernas", response.name());
    }
}