package com.example.backend.controller;

import com.example.backend.dto.TeacherDto;
import com.example.backend.entity.Teacher;
import com.example.backend.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TeacherController {
    private final TeacherService teacherService;
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    @PostMapping("/admin/teacher")
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto teacher){
        return ResponseEntity.ok(teacherService.createTeacher(teacher));
    }
    @GetMapping("/admin/teacher/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id){
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }
    @PutMapping("/admin/teacher/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody TeacherDto teacher){
        return ResponseEntity.ok(teacherService.updateTeacher(id, teacher));
    }
    @DeleteMapping("/admin/teacher/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id){
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/admin/teacher/all/{page}/{size}")
    public ResponseEntity<Page<Teacher>> getAllTeachers(@PathVariable int page, @PathVariable int size){
        return ResponseEntity.ok(teacherService.getAllTeachers(page, size));
    }
}
