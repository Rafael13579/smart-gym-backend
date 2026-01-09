package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.exception.BusinessException;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.WeekDays;
import com.academiaSpringBoot.demo.model.Workout;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.academiaSpringBoot.demo.responseDTO.WorkoutResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static com.academiaSpringBoot.demo.model.WeekDays.MONDAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Test
    void shouldCreateWorkoutSuccessfully(){
        User user = new User();
        user.setId(1L);

        WorkoutCreateDTO workoutCreateDTO = new WorkoutCreateDTO("A", MONDAY);

        Workout savedWorkout = new Workout();
        savedWorkout.setId(10L);
        savedWorkout.setName(workoutCreateDTO.name());
        savedWorkout.setUser(user);

        when(workoutRepository.save(any(Workout.class)))
                .thenReturn(savedWorkout);


        assertEquals(10L, savedWorkout.getId());
        assertEquals(workoutCreateDTO.name(), savedWorkout.getName());
        assertTrue(savedWorkout.getWorkoutExercises().isEmpty());

        verify(workoutRepository).save(any(Workout.class));

    }

    @Test
    void shouldListWorkoutByUser(){
        User user = new User();
        user.setId(1L);

        WorkoutCreateDTO workoutCreateDTO = new WorkoutCreateDTO("A", MONDAY);

        Workout savedWorkout1 = new Workout();
        savedWorkout1.setId(10L);
        savedWorkout1.setName(workoutCreateDTO.name());
        savedWorkout1.setDay(workoutCreateDTO.day());
        savedWorkout1.setUser(user);

        Workout savedWorkout2 = new Workout();
        savedWorkout2.setId(11L);
        savedWorkout2.setName("B");
        savedWorkout2.setDay(workoutCreateDTO.day());
        savedWorkout2.setUser(user);

        Workout savedWorkout3 = new Workout();
        savedWorkout3.setId(12L);
        savedWorkout3.setName("C");
        savedWorkout3.setDay(workoutCreateDTO.day());
        savedWorkout3.setUser(user);

        when(workoutRepository.findByUser(any(User.class)))
                .thenReturn(List.of(savedWorkout1, savedWorkout2, savedWorkout3));

        List<WorkoutResponseDTO> response = workoutService.listByUser(user);

        assertAll(
                () -> assertEquals(3, response.size()),
                () -> assertEquals(10L, response.getFirst().id()),
                () -> assertEquals("A", response.getFirst().name()),
                () -> assertNotNull(response.getFirst().exercises())
        );

        verify(workoutRepository).findByUser(any(User.class));
    }


    @Test
    void shouldThrowExceptionWhenWorkoutAlreadyExistsAtDay() {
        User user = new User();
        user.setId(1L);

        WorkoutCreateDTO workoutCreateDTO = new WorkoutCreateDTO("A", MONDAY);

        when(workoutRepository.existsByUserAndDay(any(User.class), any(WeekDays.class)))
                .thenReturn(true);

        BusinessException businessException = assertThrows(BusinessException.class, () -> workoutService.create(user, workoutCreateDTO));

        assertEquals("Workout already exists at this day", businessException.getMessage());
        verify(workoutRepository, times(1)).existsByUserAndDay(user, workoutCreateDTO.day());
        verify(workoutRepository, never()).save(any());
    }

    @Test
    void shouldUpdateWorkoutNameSuccefully() {
        User user = new User();
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setName("Velho Nome");

        when(workoutRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(workout));

        WorkoutResponseDTO response = workoutService.updateWorkoutName(1L, user, "Novo Nome");

        assertEquals("Novo Nome", response.name());
        verify(workoutRepository, times(1)).findByUserAndId(user, 1L);
    }


    @Test
    void shouldDeleteWorkoutSuccefully() {
        User user = new User();
        Workout workout = new Workout();
        workout.setId(1L);

        when(workoutRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(workout));

        workoutService.deleteWorkout(1L, user);

        verify(workoutRepository, times(1)).delete(workout);
    }


    @Test
    void shouldMapWorkoutToResponseDTO() {
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setName("Treino A");
        workout.setDay(MONDAY);
        workout.setWorkoutExercises(List.of());

        WorkoutResponseDTO dto = workoutService.mapResponseToDTO(workout);

        assertEquals(1L, dto.id());
        assertEquals("Treino A", dto.name());
        assertEquals(MONDAY, dto.day());
        assertTrue(dto.exercises().isEmpty());
    }

}
