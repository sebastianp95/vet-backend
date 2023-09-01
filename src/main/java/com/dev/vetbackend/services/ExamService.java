package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Exam;

import java.util.List;

public interface ExamService {
    Exam save(Exam exam, Long petId);

    List<Exam> findPetExams(Long petId);

    Exam findById(Long id);

    Exam update(Long id, Exam exam);

    void delete(Long id);
}
