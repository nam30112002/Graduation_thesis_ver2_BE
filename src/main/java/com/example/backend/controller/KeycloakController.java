package com.example.backend.controller;

import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.SignUpResponseDto;
import com.example.backend.service.KeycloakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KeycloakController {
    private final KeycloakService keycloakService;

    public KeycloakController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping("/anonymous/sign-up-student")
    public ResponseEntity<SignUpResponseDto> signUpStudent(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(keycloakService.signUpStudent(signUpDto));
    }
}
