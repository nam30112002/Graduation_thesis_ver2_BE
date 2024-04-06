package com.example.backend.service.implement;

import com.example.backend.dto.AttendanceLogDto;
import com.example.backend.dto.CourseDto;
import com.example.backend.dto.TeacherDto;
import com.example.backend.entity.*;
import com.example.backend.exception.CustomException;
import com.example.backend.repository.*;
import com.example.backend.service.TeacherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class TeacherServiceImplement implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegisterRepository registerRepository;
    private final AttendanceLogRepository attendanceLogRepository;
    public TeacherServiceImplement(TeacherRepository teacherRepository, CourseRepository courseRepository, StudentRepository studentRepository, RegisterRepository registerRepository, AttendanceLogRepository attendanceLogRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.registerRepository = registerRepository;
        this.attendanceLogRepository = attendanceLogRepository;
    }
    @Override
    public Teacher createTeacher(TeacherDto teacherDto) {
        if(teacherDto.getTeacherCode() == null && teacherDto.getKeycloakId() ==null){
            throw new CustomException("Teacher code and keycloak id is required", HttpStatus.BAD_REQUEST);
        }
        Teacher teacher = new Teacher();
        teacher.setTeacherCode(teacherDto.getTeacherCode());
        teacher.setKeycloakId(teacherDto.getKeycloakId());
        if(teacherDto.getName() != null){
            teacher.setName(teacherDto.getName());
        }
        if(teacherDto.getIsActive() != null){
            teacher.setIsActive(teacherDto.getIsActive());
        }
        teacher.setCreatedAt(OffsetDateTime.now());
        teacher.setUpdatedAt(OffsetDateTime.now());
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        if (teacherRepository.findById(id).isPresent()){
            return teacherRepository.findById(id).get();
        }else {
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Teacher updateTeacher(Long id, TeacherDto teacherDto) {
        if(teacherRepository.findById(id).isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        Teacher teacher = teacherRepository.findById(id).get();
        if(teacherDto.getTeacherCode() != null){
            teacher.setTeacherCode(teacherDto.getTeacherCode());
        }
        if(teacherDto.getName() != null){
            teacher.setName(teacherDto.getName());
        }
        if(teacherDto.getIsActive() != null){
            teacher.setIsActive(teacherDto.getIsActive());
        }
        teacher.setUpdatedAt(OffsetDateTime.now());
        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        if(teacherRepository.findById(id).isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        teacherRepository.deleteById(id);
    }

    @Override
    public Page<Teacher> getAllTeachers(int page, int size) {
        return teacherRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public void createCourse(CourseDto courseDto, String teacherKeycloakId) {
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        Course course = new Course();
        course.setCourseCode(courseDto.getCourseCode());
        course.setSubject(courseDto.getSubject());
        course.setTeacher(teacher.get());
        course.setCreatedAt(OffsetDateTime.now());
        course.setUpdatedAt(OffsetDateTime.now());
        course.setIsActive(true);
        courseRepository.save(course);
    }

    @Override
    public void addStudentToCourse(Long courseId, Long studentId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if(course.isEmpty()){
            throw new CustomException("Course not found", HttpStatus.NOT_FOUND);
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isEmpty()){
            throw new CustomException("Student not found", HttpStatus.NOT_FOUND);
        }
        RegisterId registerId = new RegisterId();
        registerId.setStudent(student.get());
        registerId.setCourse(course.get());
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()){
            throw new CustomException("Student already registered to this course", HttpStatus.BAD_REQUEST);
        }
        // Add student to course
        Register register = new Register();
        register.setId(registerId);
        register.setNumberOfAbsence(0);
        register.setNumberOfAttendance(0);
        registerRepository.save(register);
    }

    @Override
    public void addAttendance(AttendanceLogDto attendanceLogDto) {
        Optional<Student> student = studentRepository.findById(attendanceLogDto.getStudentId());
        if(student.isEmpty()){
            throw new CustomException("Student not found", HttpStatus.NOT_FOUND);
        }
        Optional<Course> course = courseRepository.findById(attendanceLogDto.getCourseId());
        if(course.isEmpty()){
            throw new CustomException("Course not found", HttpStatus.NOT_FOUND);
        }
        AttendanceLog attendanceLog = new AttendanceLog();
        attendanceLog.setStudent(student.get());
        attendanceLog.setCourse(course.get());
        attendanceLog.setAttendanceTime(attendanceLogDto.getAttendanceTime());
        attendanceLog.setIsAttendance(attendanceLogDto.getIsAttendance());
        attendanceLogRepository.save(attendanceLog);
    }
}
