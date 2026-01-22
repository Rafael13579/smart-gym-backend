package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.ai.AiProvider;
import com.academiaSpringBoot.demo.dto.createDTO.TrainingSetCreateDTO;
import com.academiaSpringBoot.demo.dto.createDTO.WorkoutCreateDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiReplaceWorkoutPlanDTO;
import com.academiaSpringBoot.demo.dto.gemini.AiGenerationRequestDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.WorkoutResponseDTO;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.ExerciseRepository;
import com.academiaSpringBoot.demo.repository.WorkoutRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiWorkoutService {

    private final AiProvider aiProvider;
    private final WorkoutService workoutService;
    private final WorkoutExerciseService workoutExerciseService;
    private final TrainingSetService trainingSetService;
    private final ExerciseRepository exerciseRepository;
    private final ObjectMapper objectMapper;
    private final WorkoutRepository workoutRepository;


    public void generateWorkoutForUser(User user, AiGenerationRequestDTO request) {
        log.info("Gerando treino para usuário ID: {}", user.getId());

        UserProfile profile = user.getProfile();
        if (profile == null) {
            throw new IllegalStateException("User does not have registered profile");
        }

        String prompt = createPrompt(user, profile, request);
        String rawResponse = aiProvider.generate(prompt);
        String jsonContent = cleanJson(rawResponse);

        try {
            List<AiWorkoutPlanDTO> plans = objectMapper.readValue(jsonContent, new TypeReference<>() {});

            saveGeneratedWorkouts(user, plans);

        } catch (Exception e) {
            log.error("Error processing JSON from AI", e);
            throw new RuntimeException("Error processing JSON from AI", e);
        }
    }

    public void replaceWorkoutPlan(User user, AiReplaceWorkoutPlanDTO dto) {
        log.info("Replacing workout for user ID: {}", user.getId());

        UserProfile profile = user.getProfile();
        if (profile == null) {
            throw new IllegalStateException("User does not have registered profile");
        }

        String prompt = replaceWorkoutPlanPrompt(user, profile, dto);
        String rawResponse = aiProvider.generate(prompt);
        String jsonContent = cleanJson(rawResponse);

        try {
            List<AiWorkoutPlanDTO> plans = objectMapper.readValue(jsonContent, new TypeReference<>() {});

            executeReplacement(user, plans);

        } catch (Exception e) {
            log.error("Error processing JSON from AI in replace", e);
            throw new RuntimeException("Error processing AI response", e);
        }
    }


    @Transactional
    protected void saveGeneratedWorkouts(User user, List<AiWorkoutPlanDTO> plans) {
        workoutRepository.deleteByUser(user);
        workoutRepository.flush();
        persistWorkouts(user, plans);
    }

    @Transactional
    protected void executeReplacement(User user, List<AiWorkoutPlanDTO> newPlans) {
        workoutRepository.deleteByUser(user);
        workoutRepository.flush();

        persistWorkouts(user, newPlans);
    }


    private void persistWorkouts(User user, List<AiWorkoutPlanDTO> plans) {
        for (AiWorkoutPlanDTO plan : plans) {

            if (plan.day() == null) continue;

            WorkoutResponseDTO workout = workoutService.create(user, new WorkoutCreateDTO(plan.workoutName(), plan.day()));

            for (AiExerciseDTO exDto : plan.exercises()) {

                if (exDto.exerciseName() == null || exDto.exerciseName().isBlank()) {
                    continue;
                }

                Exercise exercise = exerciseRepository.findByName(exDto.exerciseName())
                        .orElseGet(() -> {
                            Exercise e = new Exercise();
                            e.setName(exDto.exerciseName());
                            e.setMuscularGroup(exDto.muscularGroup() != null ? exDto.muscularGroup() : "Geral");
                            e.setDescription(exDto.description() != null ? exDto.description() : "");
                            return exerciseRepository.save(e);
                        });

                Long workoutExerciseId = workoutExerciseService.addExerciseToWorkout(workout.id(), exercise.getId(), user);

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

    public String replaceWorkoutPlanPrompt(User user, UserProfile profile, AiReplaceWorkoutPlanDTO replace) {
        return String.format("""
        Você é um personal trainer profissional, com foco em segurança, reabilitação e performance.

        O usuário solicitou a substituição do plano de treino pelo seguinte motivo:
        "%s"

        Dados do usuário:
        - Nome: %s
        - Objetivo: %s
        - Nível: %s
        - Duração por treino: %d minutos
        - Dias disponíveis: %s
        - Idade: %d
        - Peso: %.2f kg
        - Altura: %.2f m
        - Sexo: %s

        REGRAS OBRIGATÓRIAS:
        - NÃO inclua exercícios que sobrecarreguem regiões lesionadas ou mencionadas no motivo.
        - Caso o motivo envolva membros inferiores (ex: pé, joelho, tornozelo), priorize treinos de membros superiores e core.
        - Caso envolva membros superiores (ex: ombro, cotovelo, punho), priorize membros inferiores e cardio de baixo impacto.
        - Sempre priorize segurança, reabilitação e progressão gradual.
        - Nunca retorne null.
        - Todos os campos são obrigatórios.
        - Gere no máximo 5 exercícios por treino.
        - Gere no máximo 4 séries por exercício.

        O campo "day" DEVE ser exatamente um destes valores:
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY

        Retorne APENAS um JSON cru (sem markdown), exatamente neste formato:

        [
          {
            "workoutName": "Treino A - Peito e Tríceps",
            "day": "MONDAY",
            "exercises": [
              {
                "exerciseName": "Supino reto",
                "muscularGroup": "Peito",
                "description": "Exercício para peitoral, realizado com barra ou halteres.",
                "sets": [
                  { "weight": 20.0, "reps": 12 }
                ]
              }
            ]
          }
        ]
        """,
                replace.reason(),
                user.getName(),
                replace.goal(),
                replace.experienceLevel(),
                replace.durationInMinutes(),
                replace.availableDays(),
                profile.getAge(),
                profile.getWeight(),
                profile.getHeight(),
                profile.getSex()
        );
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