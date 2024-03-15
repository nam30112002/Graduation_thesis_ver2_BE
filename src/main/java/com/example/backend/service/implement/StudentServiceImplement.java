package com.example.backend.service.implement;

import com.example.backend.dto.StudentDto;
import com.example.backend.entity.Student;
import com.example.backend.exception.CustomException;
import com.example.backend.repository.StudentRepository;
import com.example.backend.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImplement implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImplement(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public Student createStudent(StudentDto studentDto) {
        if(studentDto.getStudentCode() == null || studentDto.getName() == null){
            throw new CustomException("Student code and name cannot be null", HttpStatusCode.valueOf(400));
        }
        if(studentDto.getIsActive() == null){
            studentDto.setIsActive(true);
        }
        Student student = new Student();
        student.setStudentCode(studentDto.getStudentCode());
        student.setName(studentDto.getName());
        student.setIsActive(studentDto.getIsActive());
        student.setCreatedAt(OffsetDateTime.now());
        student.setUpdatedAt(OffsetDateTime.now());
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if(student.isEmpty()){
            throw new CustomException("StudentID not found", HttpStatus.NOT_FOUND);
        }
        return student.get();
    }

    @Override
    public Student updateStudent(Long id, StudentDto studentDto) {
        boolean isUpdated = false;
        Optional<Student> studentOptional = studentRepository.findById(id);
        if(studentOptional.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
            throw new CustomException("StudentID not found", HttpStatus.NOT_FOUND);
        }
        Student student = studentOptional.get();
        if(studentDto.getStudentCode() != null){
            student.setStudentCode(studentDto.getStudentCode());
            isUpdated = true;
        }
        if(studentDto.getName() != null){
            student.setName(studentDto.getName());
            isUpdated = true;
        }
        if(studentDto.getIsActive() != null){
            student.setIsActive(studentDto.getIsActive());
            isUpdated = true;
        }
        if(isUpdated){
            student.setUpdatedAt(OffsetDateTime.now());
        }
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if(student.isEmpty()){
            throw new CustomException("StudentID not found", HttpStatus.NOT_FOUND);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public Page<Student> getAllStudents(int pageNumber, int pageSize) {
        return studentRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }
}
