package com.example.backend.service;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(StudentDto student);
    Student getStudentById(Long id);
    Student updateStudent(Long id, StudentDto student);
    void deleteStudent(Long id);
    List<Student> getAllStudents();
}
