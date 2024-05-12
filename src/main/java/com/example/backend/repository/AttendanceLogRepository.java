package com.example.backend.repository;

import com.example.backend.entity.AttendanceLog;
import com.example.backend.entity.Course;
import com.example.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    List<AttendanceLog> findByStudentAndCourse(Student student, Course course);
}