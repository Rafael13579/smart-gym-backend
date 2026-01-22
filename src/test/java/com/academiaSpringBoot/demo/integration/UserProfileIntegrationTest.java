package com.academiaSpringBoot.demo.integration;

import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.UserProfileRepository;
import com.academiaSpringBoot.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository profileRepository;

    private User user;

    @BeforeEach
    void setup() {
        profileRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder()
                .name("Rafael")
                .email("rafael@test.com")
                .password("123")
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
    }

    @Test
    @WithUserDetails(value = "rafael@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldCreateProfile() throws Exception {

        var body = """
            {
              "weight": 82.0,
              "height": 1.76,
              "age": 24,
              "sex": "MALE",
              "goal": "HYPERTROPHY",
              "experienceLevel": "INTERMEDIATE"
            }
        """;

        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(82.0))
                .andExpect(jsonPath("$.goal").value("HYPERTROPHY"));
    }

    @Test
    @WithUserDetails(value = "rafael@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldGetProfile() throws Exception {
        UserProfile profile = UserProfile.builder()
                .weight(80.0)
                .height(1.75)
                .age(23)
                .sex(UserSex.MALE)
                .goal(Goal.STRENGTH)
                .experienceLevel(ExperienceLevel.BEGINNER)
                .build();

        // Vincula e Salva
        profile.setUser(user);
        user.setProfile(profile);
        profileRepository.save(profile);
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goal").value("STRENGTH"));
    }

    @Test
    @WithUserDetails(value = "rafael@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldUpdateProfile() throws Exception {

        UserProfile profile = UserProfile.builder()
                .weight(80.0)
                .height(1.75)
                .age(23)
                .sex(UserSex.MALE)
                .build();

        profile.setUser(user);
        user.setProfile(profile);
        profileRepository.save(profile);

        var body = """
            {
              "weight": 85.0
            }
        """;

        mockMvc.perform(put("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(85.0));
    }
}