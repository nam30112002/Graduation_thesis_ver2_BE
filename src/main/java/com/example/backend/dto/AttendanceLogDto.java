package com.example.backend.dto;

import lombok.*;

import java.time.OffsetDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogDto{
    Long courseId;
    Long studentId;
    OffsetDateTime attendanceTime;
    Boolean isAttendance;
}