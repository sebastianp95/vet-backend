package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Exam;
import com.dev.vetbackend.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/{petId}")
    public ResponseEntity<?> createExam(@PathVariable Long petId, @RequestBody Exam exam) {
        Exam savedExam = examService.save(exam, petId);
        return ResponseEntity.ok(savedExam);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Exam>> getAllExamsByPet(@PathVariable("petId") Long petId) {
        List<Exam> exams = examService.findPetExams(petId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable("id") Long id) {
        Exam exam = examService.findById(id);
        return ResponseEntity.ok(exam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable("id") Long id, @RequestBody Exam exam) {
        Exam updatedExam = examService.update(id, exam);
        return ResponseEntity.ok(updatedExam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable("id") Long id) {
        examService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
