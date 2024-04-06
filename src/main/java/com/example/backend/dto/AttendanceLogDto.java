package com.example.backend.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogDto{
    Long courseId;
    Long studentId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    OffsetDateTime attendanceTime;
    Boolean isAttendance;
}