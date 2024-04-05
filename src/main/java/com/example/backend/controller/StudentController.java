package com.example.backend.controller;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;
import com.example.backend.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/user/student/all/{page}/{size}")
    public ResponseEntity<Page<Student>> getAllStudents(@PathVariable int page, @PathVariable int size){
        return ResponseEntity.ok(studentService.getAllStudents(page, size));
    }
}
