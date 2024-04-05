package com.example.backend.controller;

import com.example.backend.dto.CourseDto;
import com.example.backend.dto.TeacherDto;
import com.example.backend.entity.Teacher;
import com.example.backend.service.TeacherService;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    private KeycloakSecurityContext securityContext;
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    @PostMapping("/create-course")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto, @AuthenticationPrincipal Jwt jwt){
        System.out.println("id: " + jwt.getClaimAsString("sub"));
        teacherService.createCourse(courseDto, jwt.getClaimAsString("sub"));
        return ResponseEntity.ok("Course created successfully");
    }


}
