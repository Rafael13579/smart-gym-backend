package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.dto.createDTO.ExerciseCreateDTO;
import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.Exercise;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.dto.responseDTO.ExerciseResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private Exercise exercise;
    private ExerciseCreateDTO createDTO;

    @BeforeEach
    void setup() {
        exerciseService = new ExerciseService(exerciseRepository);

        exercise = Exercise.builder()
                .id(1L)
                .name("Bench Press")
                .muscularGroup("Chest")
                .description("Chest exercise")
                .imageUrl("bench.png")
                .build();

        createDTO = new ExerciseCreateDTO(
                "Bench Press",
                "Chest",
                "Chest exercise"
        );
    }


    @Test
    void shouldCreateExerciseSuccessfully() {
        when(exerciseRepository.save(any(Exercise.class)))
                .thenReturn(exercise);

        ExerciseResponseDTO response = exerciseService.create(createDTO);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Bench Press", response.name());
        assertEquals("Chest", response.muscularGroup());
        assertEquals("Chest exercise", response.description());

        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }


    @Test
    void shouldDeleteExerciseSuccessfully() {
        when(exerciseRepository.findById(1L))
                .thenReturn(Optional.of(exercise));

        exerciseService.deleteExercise(1L);

        verify(exerciseRepository, times(1)).delete(exercise);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingExercise() {
        when(exerciseRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> exerciseService.deleteExercise(1L)
        );

        verify(exerciseRepository, never()).delete(any());
    }


    @Test
    void shouldReturnPagedExercises() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise));

        when(exerciseRepository.findAll(pageable))
                .thenReturn(page);

        Page<ExerciseResponseDTO> result = exerciseService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Bench Press", result.getContent().getFirst().name());
    }


    @Test
    void shouldAutocompleteExercises() {
        Page<Exercise> page = new PageImpl<>(List.of(exercise));

        when(exerciseRepository.findByNameStartingWithIgnoreCase(eq("Ben"), any(Pageable.class)))
                .thenReturn(page);

        List<ExerciseResponseDTO> result = exerciseService.autocompleteExercise(" Ben ");

        assertEquals(1, result.size());
        assertEquals("Bench Press", result.getFirst().name());

        verify(exerciseRepository, times(1))
                .findByNameStartingWithIgnoreCase(eq("Ben"), any(Pageable.class));
    }


}
