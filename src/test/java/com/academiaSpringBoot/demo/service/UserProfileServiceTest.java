package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.dto.createDTO.UserProfileCreateDTO;
import com.academiaSpringBoot.demo.exception.BusinessException;
import com.academiaSpringBoot.demo.model.*;
import com.academiaSpringBoot.demo.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService service;

    @Mock
    private UserProfileRepository repository;

    private User mockUser() {
        return User.builder()
                .id(1L)
                .email("test@email.com")
                .name("Rafael")
                .role(User.Role.USER)
                .build();
    }

    @Test
    void shouldCreateProfile() {
        User user = mockUser();

        UserProfileCreateDTO dto = new UserProfileCreateDTO(
                80.0, 1.75, 23,
                UserSex.MALE,
                Goal.HYPERTROPHY,
                ExperienceLevel.INTERMEDIATE
        );

        when(repository.existsByUser(user)).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = service.create(user, dto);

        assertEquals(80.0, response.weight());
        assertEquals(Goal.HYPERTROPHY, response.goal());
        verify(repository).save(any(UserProfile.class));
    }

    @Test
    void shouldNotCreateDuplicateProfile() {
        User user = mockUser();

        when(repository.existsByUser(user)).thenReturn(true);

        assertThrows(BusinessException.class, () ->
                service.create(user, new UserProfileCreateDTO(null,null,null,null,null,null))
        );
    }

    @Test
    void shouldUpdateProfile() {
        User user = mockUser();

        UserProfile profile = UserProfile.builder()
                .user(user)
                .weight(70.0)
                .build();

        when(repository.findByUser(user)).thenReturn(Optional.of(profile));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var response = service.update(user,
                new UserProfileCreateDTO(75.0, null, null, null, null, null));

        assertEquals(75.0, response.weight());
    }
}
