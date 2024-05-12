package com.example.backend.repository;

import com.example.backend.entity.Course;
import com.example.backend.entity.Register;
import com.example.backend.entity.RegisterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<Register, RegisterId> {
    Optional<Register> findById(RegisterId registerId);

    List<Register> findByIdCourse(Course course);
}