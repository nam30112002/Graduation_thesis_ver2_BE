package com.example.backend.service.implement;

import com.example.backend.dto.AttendanceLogDto;
import com.example.backend.dto.CourseDto;
import com.example.backend.dto.QuestionDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImplement implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegisterRepository registerRepository;
    private final AttendanceLogRepository attendanceLogRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    public TeacherServiceImplement(TeacherRepository teacherRepository, CourseRepository courseRepository, StudentRepository studentRepository, RegisterRepository registerRepository, AttendanceLogRepository attendanceLogRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.registerRepository = registerRepository;
        this.attendanceLogRepository = attendanceLogRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
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
    public void addStudentToCourse(Long courseId, Long studentId, String teacherKeycloakId) {
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
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!course.get().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
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
        //check if student is registered to the course
        RegisterId registerId = new RegisterId();
        registerId.setStudent(student.get());
        registerId.setCourse(course.get());
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isEmpty()){
            throw new CustomException("Student is not registered to this course", HttpStatus.BAD_REQUEST);
        }
        AttendanceLog attendanceLog = new AttendanceLog();
        attendanceLog.setStudent(student.get());
        attendanceLog.setCourse(course.get());
        attendanceLog.setAttendanceTime(attendanceLogDto.getAttendanceTime());
        attendanceLog.setIsAttendance(attendanceLogDto.getIsAttendance());
        attendanceLogRepository.save(attendanceLog);
    }

    @Override
    public List<Course> getMyCourses(String teacherKeycloakId) {
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseRepository.findByTeacher(teacher.get());
        List<Course> response = new ArrayList<>();
        for (Course course: courses){
            Course courseWithoutTeacher = new Course();
            courseWithoutTeacher.setId(course.getId());
            courseWithoutTeacher.setCourseCode(course.getCourseCode());
            courseWithoutTeacher.setSubject(course.getSubject());
            courseWithoutTeacher.setCreatedAt(course.getCreatedAt());
            courseWithoutTeacher.setUpdatedAt(course.getUpdatedAt());
            courseWithoutTeacher.setIsActive(course.getIsActive());
            response.add(courseWithoutTeacher);
        }
        return response;
    }

    @Override
    public void deleteStudentFromCourse(Long courseId, Long studentId, String teacherKeycloakId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if(course.isEmpty()){
            throw new CustomException("Course not found", HttpStatus.NOT_FOUND);
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isEmpty()){
            throw new CustomException("Student not found", HttpStatus.NOT_FOUND);
        }
        //check if student is registered to the course
        RegisterId registerId = new RegisterId();
        registerId.setStudent(student.get());
        registerId.setCourse(course.get());
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isEmpty()){
            throw new CustomException("Student is not registered to this course", HttpStatus.BAD_REQUEST);
        }
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!course.get().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
        }
        registerRepository.deleteById(registerId);
    }

    @Override
    public void deleteAttendance(Long attendanceId, String teacherKeycloakId) {
        Optional<AttendanceLog> attendanceLog = attendanceLogRepository.findById(attendanceId);
        if(attendanceLog.isEmpty()){
            throw new CustomException("Attendance not found", HttpStatus.NOT_FOUND);
        }
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!attendanceLog.get().getCourse().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
        }
        attendanceLogRepository.deleteById(attendanceId);
    }

    @Override
    public void createQuestion(Long courseId, QuestionDto questionDto, String teacherKeycloakId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if(course.isEmpty()){
            throw new CustomException("Course not found", HttpStatus.NOT_FOUND);
        }
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!course.get().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
        }
        Question question = new Question();
        question.setCourse(course.get());
        question.setContent(questionDto.getQuestion());
        questionRepository.save(question);
        for(String correctAnswer: questionDto.getCorrectAnswers()){
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setIsTrue(true);
            //if answer starts with "@image " then it is image answer
            if(correctAnswer.startsWith("@image ")){
                answer.setContent(correctAnswer.substring(7));
                answer.setIsImage(true);
            }else{
                answer.setContent(correctAnswer);
                answer.setIsImage(false);
            }
            answerRepository.save(answer);
        }
        for(String wrongAnswer: questionDto.getWrongAnswers()){
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setIsTrue(false);
            //if answer starts with "@image " then it is image answer
            if(wrongAnswer.startsWith("@image ")){
                answer.setContent(wrongAnswer.substring(7));
                answer.setIsImage(true);
            }else{
                answer.setContent(wrongAnswer);
                answer.setIsImage(false);
            }
            answerRepository.save(answer);
        }
    }

    @Override
    public void deleteQuestion(Long questionId, String teacherKeycloakId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if(question.isEmpty()){
            throw new CustomException("Question not found", HttpStatus.NOT_FOUND);
        }
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!question.get().getCourse().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
        }
        questionRepository.deleteById(questionId);
        List<Answer> answers = answerRepository.findByQuestion(question.get());
        for(Answer answer: answers){
            answerRepository.deleteById(answer.getId());
        }
    }

    @Override
    public void deleteAnswer(Long answerId, String teacherKeycloakId) {
        Optional<Answer> answer = answerRepository.findById(answerId);
        if(answer.isEmpty()){
            throw new CustomException("Answer not found", HttpStatus.NOT_FOUND);
        }
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!answer.get().getQuestion().getCourse().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
        }
        answerRepository.deleteById(answerId);
    }

    @Override
    public void updateAnswer(Long answerId, String content, String teacherKeycloakId) {
        Optional<Answer> answer = answerRepository.findById(answerId);
        if(answer.isEmpty()){
            throw new CustomException("Answer not found", HttpStatus.NOT_FOUND);
        }
        //check if course is mine
        Optional<Teacher> teacher = teacherRepository.findByKeycloakId(teacherKeycloakId);
        if(teacher.isEmpty()){
            throw new CustomException("Teacher not found", HttpStatus.NOT_FOUND);
        }
        if(!answer.get().getQuestion().getCourse().getTeacher().equals(teacher.get())){
            throw new CustomException("Course is not yours", HttpStatus.BAD_REQUEST);
        }
        if(content.startsWith("@image ")){
            answer.get().setIsImage(true);
            answer.get().setContent(content.substring(7));
        }else{
            answer.get().setIsImage(false);
            answer.get().setContent(content);
        }
    }

    @Override
    public List<?> getAllQuestionOfCourse(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if(course.isEmpty()){
            throw new CustomException("Course not found", HttpStatus.NOT_FOUND);
        }
        List<Question> questions = questionRepository.findByCourse(course.get());
        List<Question> response = new ArrayList<>();
        for(Question question: questions){
            Question questionWithoutCourse = new Question();
            questionWithoutCourse.setId(question.getId());
            questionWithoutCourse.setContent(question.getContent());
            questionWithoutCourse.setCreatedAt(question.getCreatedAt());
            questionWithoutCourse.setUpdatedAt(question.getUpdatedAt());
            response.add(questionWithoutCourse);
        }
        return response;
    }

    @Override
    public List<?> getAllAnswerOfQuestion(Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if(question.isEmpty()){
            throw new CustomException("Question not found", HttpStatus.NOT_FOUND);
        }
        List<Answer> answers = answerRepository.findByQuestion(question.get());
        List<Answer> response = new ArrayList<>();
        for(Answer answer: answers){
            Answer answerWithoutQuestion = new Answer();
            answerWithoutQuestion.setId(answer.getId());
            answerWithoutQuestion.setContent(answer.getContent());
            answerWithoutQuestion.setIsImage(answer.getIsImage());
            answerWithoutQuestion.setIsTrue(answer.getIsTrue());
            answerWithoutQuestion.setCreatedAt(answer.getCreatedAt());
            answerWithoutQuestion.setUpdatedAt(answer.getUpdatedAt());
            response.add(answerWithoutQuestion);
        }
        return response;
    }
}
