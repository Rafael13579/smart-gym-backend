package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.client.GeminiClient;
import com.academiaSpringBoot.demo.dto.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.dto.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiGenerationRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.GeminiRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.GeminiResponseDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.WorkoutResponseDTO;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiWorkoutService {

    private final GeminiClient geminiClient;
    private final WorkoutService workoutService;
    private final WorkoutExerciseService workoutExerciseService;
    private final ObjectMapper objectMapper;
    private final ExerciseRepository exerciseRepository;
    private final TrainingSetService trainingSetService;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiWorkoutService(
            GeminiClient geminiClient,
            WorkoutService workoutService,
            WorkoutExerciseService workoutExerciseService,
            ObjectMapper objectMapper,
            ExerciseRepository exerciseRepository,
            TrainingSetService trainingSetService
    ) {
        this.geminiClient = geminiClient;
        this.workoutService = workoutService;
        this.workoutExerciseService = workoutExerciseService;
        this.objectMapper = objectMapper;
        this.exerciseRepository = exerciseRepository;
        this.trainingSetService = trainingSetService;
    }

    @Transactional
    public void generateWorkoutForUser(User user, AiGenerationRequestDTO request) {

        String prompt = createPrompt(user, request);

        GeminiRequestDTO geminiRequest = GeminiRequestDTO.fromPrompt(prompt);
        GeminiResponseDTO response = geminiClient.generateContent(apiKey, geminiRequest);

        String jsonContent = cleanJson(response.getText());

        try {
            List<AiWorkoutPlanDTO> plans =
                    objectMapper.readValue(jsonContent, new TypeReference<>() {});
            persistWorkouts(user, plans);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar treino por IA", e);
        }
    }

    private String createPrompt(User user, AiGenerationRequestDTO request) {
        String days = request.availableDays().toString();

        return String.format("""
            Atue como um personal trainer.
            
            Gere um plano de treino para:
            - Nome: %s
            - Objetivo: %s
            - Nível: %s
            - Duração: %d minutos
            - Dias disponíveis: %s

            Retorne APENAS um JSON cru (sem markdown), exatamente neste formato:

            [
              {
                "workoutName": "Treino A - Peito",
                "day": "MONDAY",
                "exercises": [
                  {
                    "exerciseName": "Supino reto",
                    "muscularGroup": "Peito",
                    "description": "Exercício para peitoral",
                    "sets": [
                      { "weight": 20.0, "reps": 12 }
                    ]
                  }
                ]
              }
            ]

            Regras obrigatórias:
            - Nunca retorne null
            - Todos os campos são obrigatórios
            - Dias da semana em inglês (MONDAY, TUESDAY...)
            """,
                user.getName(),
                request.goal(),
                request.experienceLevel(),
                request.durationInMinutes(),
                days
        );
    }

    private void persistWorkouts(User user, List<AiWorkoutPlanDTO> plans) {

        for (AiWorkoutPlanDTO plan : plans) {

            WorkoutResponseDTO workout = workoutService.create(user, new WorkoutCreateDTO(plan.workoutName(), plan.day()));

            for (AiExerciseDTO exDto : plan.exercises()) {

                if (exDto.exerciseName() == null || exDto.exerciseName().isBlank()) {
                    throw new RuntimeException("IA retornou exercício sem nome");
                }

                String muscularGroup =
                        exDto.muscularGroup() != null && !exDto.muscularGroup().isBlank()
                                ? exDto.muscularGroup()
                                : "Indefinido";

                String description =
                        exDto.description() != null && !exDto.description().isBlank()
                                ? exDto.description()
                                : "Exercício gerado automaticamente";

                Exercise exercise = exerciseRepository
                        .findByName(exDto.exerciseName())
                        .orElseGet(() -> {
                            Exercise e = new Exercise();
                            e.setName(exDto.exerciseName());
                            e.setMuscularGroup(muscularGroup);
                            e.setDescription(description);
                            return exerciseRepository.save(e);
                        });

                Long workoutExerciseId =
                        workoutExerciseService.addExerciseToWorkout(
                                workout.id(),
                                exercise.getId(),
                                user
                        );

                if (exDto.sets() != null) {
                    for (AiSetDTO setAi : exDto.sets()) {

                        Double weight = setAi.weight() != null ? setAi.weight() : 10.0;
                        Integer reps = setAi.reps() != null ? setAi.reps() : 12;

                        TrainingSetCreateDTO setDTO =
                                new TrainingSetCreateDTO(weight, reps);

                        trainingSetService.createQuickSet(workoutExerciseId, setDTO);
                    }
                }
            }
        }
    }

    private String cleanJson(String content) {
        if (content == null) return "";
        if (content.startsWith("```json")) {
            return content.replace("```json", "").replace("```", "").trim();
        }
        if (content.startsWith("```")) {
            return content.replace("```", "").trim();
        }
        return content.trim();
    }


    private record AiWorkoutPlanDTO(
            String workoutName,
            WeekDays day,
            List<AiExerciseDTO> exercises
    ) {}

    private record AiExerciseDTO(
            String exerciseName,
            String muscularGroup,
            String description,
            List<AiSetDTO> sets
    ) {}

    private record AiSetDTO(
            Double weight,
            Integer reps
    ) {}
}
