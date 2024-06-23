package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.Form;
import com.example.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByForm(Form form);
}