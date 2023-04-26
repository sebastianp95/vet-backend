package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.MedicalHistory;

import java.util.List;

public interface MedicalHistoryService {

    MedicalHistory save(MedicalHistory medicalHistory, Long petId);

    List<MedicalHistory> findPetMedicalRecords(Long id);

    MedicalHistory findById(Long id);

    MedicalHistory update(Long id, MedicalHistory medicalHistory);

    void delete(Long id);
}
