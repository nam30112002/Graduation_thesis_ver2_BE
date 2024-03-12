package com.example.backend.service.implement;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;
import com.example.backend.exception.CustomException;
import com.example.backend.repository.StudentRepository;
import com.example.backend.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImplement implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImplement(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public Student createStudent(StudentDto studentDto) {
        if(studentDto.getStudentCode() == null || studentDto.getName() == null){
            throw new CustomException("Student code and name cannot be null", HttpStatus.BAD_REQUEST);
        }
        if(studentDto.getIsActive() == null){
            studentDto.setIsActive(true);
        }
        Student student = new Student();
        student.setStudentCode(studentDto.getStudentCode());
        student.setName(studentDto.getName());
        student.setIsActive(studentDto.getIsActive());
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return null;
    }

    @Override
    public Student updateStudent(Long id, StudentDto student) {
        return null;
    }

    @Override
    public void deleteStudent(Long id) {

    }

    @Override
    public List<Student> getAllStudents() {
        return null;
    }
}
