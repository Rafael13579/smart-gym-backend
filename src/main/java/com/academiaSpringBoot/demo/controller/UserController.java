package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.createDTO.UserCreateDTO;
import com.academiaSpringBoot.demo.responseDTO.UserResponseDTO;
import com.academiaSpringBoot.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(userService.createUser(dto));
    }

    @GetMapping
    public List<UserResponseDTO> listAll() {
        return userService.listAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
