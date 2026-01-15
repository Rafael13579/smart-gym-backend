package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.ai.AiProvider;
import com.academiaSpringBoot.demo.dto.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.dto.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiReplaceWorkoutPlanDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiGenerationRequestDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.WorkoutResponseDTO;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AiWorkoutService {

    private final AiProvider aiProvider;
    private final WorkoutService workoutService;
    private final WorkoutExerciseService workoutExerciseService;
    private final TrainingSetService trainingSetService;
    private final ExerciseRepository exerciseRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void generateWorkoutForUser(User user, AiGenerationRequestDTO request) {

        UserProfile profile = user.getProfile();

        if (profile == null) {
            throw new IllegalStateException("Usuário não possui perfil cadastrado");
        }

        String prompt = createPrompt(user, profile, request);

        String rawResponse = aiProvider.generate(prompt);
        String jsonContent = cleanJson(rawResponse);

        try {
            List<AiWorkoutPlanDTO> plans = objectMapper.readValue(jsonContent, new TypeReference<>() {});

            persistWorkouts(user, plans);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da IA", e);
        }
    }


    private String createPrompt(User user, UserProfile profile, AiGenerationRequestDTO request) {

        return String.format("""
            Atue como um personal trainer profissional.

            Gere um plano de treino para o seguinte usuário:
            - Nome: %s
            - Objetivo: %s
            - Nível: %s
            - Duração: %d minutos
            - Dias disponíveis: %s
            - Idade: %d
            - Peso: %.2f
            - Altura: %.2f
            - Sexo: %s

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
            - "day" DEVE ser um dos valores:
              MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
            - Gere no máximo 5 exercícios por treino
            - Gere no máximo 4 séries por exercício
            """,
                user.getName(),
                request.goal(),
                request.experienceLevel(),
                request.durationInMinutes(),
                request.availableDays(),
                profile.getAge(),
                profile.getWeight(),
                profile.getHeight(),
                profile.getSex()
        );
    }


    private void persistWorkouts(User user, List<AiWorkoutPlanDTO> plans) {

        for (AiWorkoutPlanDTO plan : plans) {

            WorkoutResponseDTO workout = workoutService.create(user, new WorkoutCreateDTO(plan.workoutName(), plan.day()));

            for (AiExerciseDTO exDto : plan.exercises()) {

                if (exDto.exerciseName() == null || exDto.exerciseName().isBlank()) {
                    throw new IllegalStateException("IA retornou exercício sem nome");
                }

                Exercise exercise = exerciseRepository.findByName(exDto.exerciseName())
                        .orElseGet(() -> {
                            Exercise e = new Exercise();
                            e.setName(exDto.exerciseName());
                            e.setMuscularGroup(exDto.muscularGroup());
                            e.setDescription(exDto.description());
                            return exerciseRepository.save(e);
                        });

                Long workoutExerciseId =
                        workoutExerciseService.addExerciseToWorkout(workout.id(), exercise.getId(), user);

                if (exDto.sets() != null) {
                    for (AiSetDTO setAi : exDto.sets()) {

                        TrainingSetCreateDTO setDTO = new TrainingSetCreateDTO(
                                        setAi.weight() != null ? setAi.weight() : 10.0,
                                        setAi.reps() != null ? setAi.reps() : 12
                                );

                        trainingSetService.createQuickSet(workoutExerciseId, setDTO);
                    }
                }
            }
        }
    }

    public String replaceWorkoutPlanPrompt(User user, UserProfile profile, AiReplaceWorkoutPlanDTO replace) {
        return String.format("""
            Atue como um personal trainer profissional.

            Substitua o plano de treino existente para o seguinte usuário com base na razão da solicitação:
            - Nome: %s
            - Objetivo: %s
            - Nível: %s
            - Duração: %d minutos
            - Dias disponíveis: %s
            - Idade: %d
            - Peso: %.2f
            - Altura: %.2f
            - Sexo: %s
            - Razão: %s

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
            - "day" DEVE ser um dos valores:
              MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
            - Gere no máximo 5 exercícios por treino
            - Gere no máximo 4 séries por exercício
            """,
                user.getName(),
                replace.goal(),
                replace.experienceLevel(),
                replace.durationInMinutes(),
                replace.availableDays(),
                profile.getAge(),
                profile.getWeight(),
                profile.getHeight(),
                profile.getSex(),
                replace.reason()

        );

    }

    public void replaceWorkoutPlan(User user, AiReplaceWorkoutPlanDTO dto) {

        UserProfile profile = user.getProfile();

        if (profile == null) {
            throw new IllegalStateException("Usuário não possui perfil cadastrado");
        }

        String prompt = replaceWorkoutPlanPrompt(user, profile, dto);

        String rawResponse = aiProvider.generate(prompt);
        String jsonContent = cleanJson(rawResponse);

        try {
            List<AiWorkoutPlanDTO> plans = objectMapper.readValue(jsonContent, new TypeReference<>() {});

            persistWorkouts(user, plans);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da IA", e);
        }
    }


    private String cleanJson(String content) {
        if (content == null) return "";
        return content
                .replace("```json", "")
                .replace("```", "")
                .trim();
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
