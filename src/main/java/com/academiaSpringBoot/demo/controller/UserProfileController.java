package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.dto.createDTO.UserProfileCreateDTO;
import com.academiaSpringBoot.demo.dto.responseDTO.UserProfileResponseDTO;
import com.academiaSpringBoot.demo.model.User;

import com.academiaSpringBoot.demo.service.UserProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Profile", description = "Gerencia o perfil do usuário")
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> createUserProfile(@RequestBody UserProfileCreateDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(userProfileService.create(user, dto));
    }

    @PutMapping
    public UserProfileResponseDTO update(@RequestBody UserProfileCreateDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return userProfileService.update(user, dto);
    }

    @GetMapping
    public UserProfileResponseDTO getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return userProfileService.getByUser(user);
    }
}

