package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "question")
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

}