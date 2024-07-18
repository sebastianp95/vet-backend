package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Asset;
import com.dev.vetbackend.entity.Exam;
import com.dev.vetbackend.services.ExamService;
import com.dev.vetbackend.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;
    @Autowired
    private S3Service s3Service;

    @PostMapping("/{petId}")
    public ResponseEntity<?> createExam(@PathVariable Long petId, @RequestBody Exam exam) {
        Exam savedExam = examService.save(exam, petId);
        return ResponseEntity.ok(savedExam);
    }

    @PostMapping("/uploadFile/{petId}")
    public Map<String, String> uploadFileToS3(
            @PathVariable Long petId,
            @RequestParam("file") MultipartFile file
    ) {

        String key = s3Service.putObject(file);
        Map<String, String> response = new HashMap<>();
        response.put("key", key);
        response.put("url", s3Service.getObjectUrl(key));

        return response;
    }


    @GetMapping(value = "/get-object", params = "key")
    public ResponseEntity<ByteArrayResource> getObject(@RequestParam String key) {
        Asset asset = s3Service.getObject(key);
        ByteArrayResource resource = new ByteArrayResource(asset.getContent());

        return ResponseEntity.ok()
                .header("Content-Type", asset.getContentType())
                .contentLength(asset.getContent().length)
                .body(resource);
    }

    @DeleteMapping(value = "/delete-object", params = "key")
    public void deleteObject(@RequestParam String key) {
        s3Service.deleteObject(key);
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
