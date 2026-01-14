package com.academiaSpringBoot.demo.service;

import com.academiaSpringBoot.demo.dto.createDTO.UserProfileCreateDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.UserProfileResponseDTO;
import com.academiaSpringBoot.demo.exception.BusinessException;
import com.academiaSpringBoot.demo.exception.ResourceNotFoundException;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.model.UserProfile;
import com.academiaSpringBoot.demo.repository.UserProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserProfileResponseDTO create(User user, UserProfileCreateDTO dto) {

        if (repository.existsByUser(user)) {
            throw new BusinessException("User profile already exists");
        }

        UserProfile profile = UserProfile.builder()
                .user(user)
                .weight(dto.weight())
                .height(dto.height())
                .age(dto.age())
                .sex(dto.sex())
                .goal(dto.goal())
                .experienceLevel(dto.experienceLevel())
                .build();

        return mapToDTO(repository.save(profile));
    }

    @Transactional
    public UserProfileResponseDTO update(User user, UserProfileCreateDTO dto) {
        UserProfile profile = repository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (dto.weight() != null) profile.setWeight(dto.weight());
        if (dto.height() != null) profile.setHeight(dto.height());
        if (dto.age() != null) profile.setAge(dto.age());
        if (dto.sex() != null) profile.setSex(dto.sex());
        if (dto.goal() != null) profile.setGoal(dto.goal());
        if (dto.experienceLevel() != null) profile.setExperienceLevel(dto.experienceLevel());

        return mapToDTO(repository.save(profile));
    }

    @Transactional
    public UserProfileResponseDTO getByUser(User user) {
        return mapToDTO(
                repository.findByUser(user)
                        .orElseThrow(() -> new ResourceNotFoundException("Profile not found"))
        );
    }

    private UserProfileResponseDTO mapToDTO(UserProfile profile) {
        return new UserProfileResponseDTO(
                profile.getId(),
                profile.getWeight(),
                profile.getHeight(),
                profile.getAge(),
                profile.getSex(),
                profile.getGoal(),
                profile.getExperienceLevel()
        );
    }
}
