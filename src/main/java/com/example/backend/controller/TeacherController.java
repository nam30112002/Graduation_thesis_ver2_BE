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
        System.out.println("id: " + jwt.getClaimAsString("sub")); //sub is the keycloak id
        teacherService.createCourse(courseDto, jwt.getClaimAsString("sub"));
        return ResponseEntity.ok("Course created successfully");
    }

    @PostMapping("/add-student-to-course")
    public ResponseEntity<?> addStudentToCourse(@AuthenticationPrincipal Jwt jwt, @RequestParam Long courseId, @RequestParam Long studentId){
        teacherService.addStudentToCourse(courseId, studentId, jwt.getClaimAsString("sub"));
        return ResponseEntity.ok("Student added to course successfully");
    }
    @PostMapping("/add-attendance")
    public ResponseEntity<?> addAttendance(@RequestBody AttendanceLogDto attendanceLogDto){
        teacherService.addAttendance(attendanceLogDto);
        return ResponseEntity.ok("Attendance added successfully");
    }
    @GetMapping("/get-my-courses")
    public ResponseEntity<?> getMyCourses(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(teacherService.getMyCourses(jwt.getClaimAsString("sub")));
    }
    @DeleteMapping("/delete-student-from-course")
    public ResponseEntity<?> deleteStudentFromCourse(@RequestParam Long courseId, @RequestParam Long studentId, @AuthenticationPrincipal Jwt jwt){
        teacherService.deleteStudentFromCourse(courseId, studentId, jwt.getClaimAsString("sub"));
        return ResponseEntity.ok("Student deleted from course successfully");
    }
}
