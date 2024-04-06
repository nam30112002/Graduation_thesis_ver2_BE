package com.example.backend.repository;

import com.example.backend.entity.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
}