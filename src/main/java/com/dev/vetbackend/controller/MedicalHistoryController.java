package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.MedicalHistory;
import com.dev.vetbackend.services.MedicalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @PostMapping("/{petId}")
    public ResponseEntity<?> createMedicalHistory(@PathVariable Long petId, @RequestBody MedicalHistory medicalHistory) {
        MedicalHistory savedMedicalHistory = medicalHistoryService.save(medicalHistory, petId);
        return ResponseEntity.ok(savedMedicalHistory);

    }


    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalHistory>> getAllMedicalHistories(@PathVariable("petId") Long id) {
        List<MedicalHistory> medicalHistories = medicalHistoryService.findPetMedicalRecords(id);
        return ResponseEntity.ok(medicalHistories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalHistory> getMedicalHistoryById(@PathVariable("id") Long id) {
        MedicalHistory medicalHistory = medicalHistoryService.findById(id);
        return ResponseEntity.ok(medicalHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistory> updateMedicalHistory(@PathVariable("id") Long id, @RequestBody MedicalHistory medicalHistory) {
        MedicalHistory updatedMedicalHistory = medicalHistoryService.update(id, medicalHistory);
        return ResponseEntity.ok(updatedMedicalHistory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalHistory(@PathVariable("id") Long id) {
        medicalHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
