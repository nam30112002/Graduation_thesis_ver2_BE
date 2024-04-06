package com.example.backend.controller;

import com.example.backend.dto.AttendanceLogDto;
import com.example.backend.dto.CourseDto;
import com.example.backend.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }
    @PostMapping("/create-course")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto, @AuthenticationPrincipal Jwt jwt){
        System.out.println("id: " + jwt.getClaimAsString("sub"));
        teacherService.createCourse(courseDto, jwt.getClaimAsString("sub"));
        return ResponseEntity.ok("Course created successfully");
    }

    @PostMapping("/add-student-to-course")
    public ResponseEntity<?> addStudentToCourse(@RequestParam Long courseId, @RequestParam Long studentId){
        teacherService.addStudentToCourse(courseId, studentId);
        return ResponseEntity.ok("Student added to course successfully");
    }
    @PostMapping("/add-attendance")
    public ResponseEntity<?> addAttendance(@RequestBody AttendanceLogDto attendanceLogDto){
        teacherService.addAttendance(attendanceLogDto);
        return ResponseEntity.ok("Attendance added successfully");
    }
}
