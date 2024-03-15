package com.example.backend.service;

import com.example.backend.dto.TeacherDto;
import com.example.backend.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
public interface TeacherService {
    Teacher createTeacher(TeacherDto teacherDto);
    Teacher getTeacherById(Long id);
    Teacher updateTeacher(Long id, TeacherDto teacherDto);
    void deleteTeacher(Long id);
    Page<Teacher> getAllTeachers(int page, int size);
}
