package com.example.backend.controller;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;
import com.example.backend.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping("/user/student")
    public ResponseEntity<Student> createStudent(@RequestBody StudentDto student){
        return ResponseEntity.ok(studentService.createStudent(student));
    }
}
