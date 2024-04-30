package com.example.backend.repository;

import com.example.backend.entity.Answer;
import com.example.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion(Question question);
}