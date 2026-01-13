package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.dto.createDTO.ExerciseCreateDTO;
import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.Exercise;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.dto.responseDTO.ExerciseResponseDTO;
import com.academiaSpringBoot.demo.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExerciseIntegrationTest {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;


    @Test
    void shouldCreateExercise() {
        ExerciseCreateDTO dto = new ExerciseCreateDTO("Supino Reto", "Peito", "Exercicio para peito");

        exerciseService.create(dto);

        String name = exerciseRepository.findById(1L).get().getName();
        int size = exerciseRepository.findAll().size();

        assertEquals("Supino Reto", name);
        assertEquals(1, size);

    }

    @Test
    void shouldDeleteExercise() {
        Exercise ex = new Exercise();
        ex.setName("Supino Reto");
        ex.setMuscularGroup("Peito");
        ex.setDescription("Exercicio para peito");
        exerciseRepository.save(ex);

        Exercise savedExercise = exerciseRepository.findAll().getFirst();

        exerciseRepository.delete(savedExercise);

        assertEquals(0, exerciseRepository.findAll().size());
        assertThrows(ResourceNotFoundException.class, () ->
                        { exerciseRepository.findById(1L)
                                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found exception"));
                        });
    }

    @Test
    void shouldAutocompleteExercise() {
        Exercise ex = new Exercise();
        ex.setName("Supino Reto");
        ex.setMuscularGroup("Peito");
        ex.setDescription("Exercicio para peito");
        exerciseRepository.save(ex);

       List<ExerciseResponseDTO> response = exerciseService.autocompleteExercise("sup");

        assertEquals("Supino Reto", response.getFirst().name());
    }

    @Test
    void shouldFindExerciseById() {
        Exercise ex = new Exercise();
        ex.setName("Leg Press");
        ex.setMuscularGroup("Pernas");
        ex.setDescription("Empurrar com as pernas");
        Exercise saved = exerciseRepository.save(ex);

        Optional<Exercise> response = exerciseRepository.findById(saved.getId());

        assertEquals("Leg Press", response.get().getName());
    }

    @Test
    void shouldThrowExceptionWhenExerciseNotFound() {
        Long nonExistentId = 999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            exerciseRepository.findById(nonExistentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found exception"));
        });
    }

    @Test
    void shouldUpdateExercise() {
        Exercise ex = new Exercise();
        ex.setName("Supino Reto");
        ex.setMuscularGroup("Peito");
        ex.setDescription("Descrição antiga");
        Exercise savedExercise = exerciseRepository.save(ex);

        ExerciseCreateDTO updateDto = new ExerciseCreateDTO("Supino Inclinado", "Peito", "Descrição nova");
        exerciseService.partialUpdate(savedExercise.getId(), updateDto);

        Exercise updatedExercise = exerciseRepository.findById(savedExercise.getId()).orElseThrow();

        assertEquals("Supino Inclinado", updatedExercise.getName());
        assertEquals("Descrição nova", updatedExercise.getDescription());
    }
}
