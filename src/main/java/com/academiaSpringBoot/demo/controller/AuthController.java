package com.academiaSpringBoot.demo.controller;

import com.academiaSpringBoot.demo.loginDTO.LoginRequestDTO;
import com.academiaSpringBoot.demo.model.User;
import com.academiaSpringBoot.demo.registerRequestDTO.RegisterRequestDTO;
import com.academiaSpringBoot.demo.repository.UserRepository;
import com.academiaSpringBoot.demo.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto) {
        System.out.println("🔥 ENTROU NO LOGIN");
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(User.Role.USER)
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
