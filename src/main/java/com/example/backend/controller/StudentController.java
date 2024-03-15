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
    @PostMapping("/user/student")
    public ResponseEntity<Student> createStudent(@RequestBody StudentDto student){
        return ResponseEntity.ok(studentService.createStudent(student));
    }
    @GetMapping("/user/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
    @PutMapping("/user/student/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody StudentDto student){
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }
    @DeleteMapping("/user/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/user/student/all/{page}/{size}")
    public ResponseEntity<Page<Student>> getAllStudents(@PathVariable int page, @PathVariable int size){
        return ResponseEntity.ok(studentService.getAllStudents(page, size));
    }
}
