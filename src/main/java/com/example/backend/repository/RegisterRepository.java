package com.example.backend.repository;

import com.example.backend.entity.Register;
import com.example.backend.entity.RegisterId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterRepository extends JpaRepository<Register, RegisterId> {
    Optional<Register> findById(RegisterId registerId);
}