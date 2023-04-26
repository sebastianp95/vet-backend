package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.MedicalHistory;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.repository.MedicalHistoryRepository;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    public MedicalHistory save(MedicalHistory medicalHistory, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found for ID: " + petId));
        medicalHistory.setPet(pet);

        User user = userDetailServiceImpl.getAuthenticatedUser();
        medicalHistory.setUser(user);

        return medicalHistoryRepository.save(medicalHistory);
    }
    @Override
    public List<MedicalHistory> findPetMedicalRecords(Long id) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<MedicalHistory> histories = medicalHistoryRepository.findAllByUserAndPetId(user, id);
        return histories;
    }

    @Override
    public MedicalHistory findById(Long id) {
        Optional<MedicalHistory> medicalHistoryOptional = medicalHistoryRepository.findById(Math.toIntExact(id));
        return medicalHistoryOptional.orElseThrow(() -> new RuntimeException("Medical History not found for ID: " + id));
    }

    @Override
    public MedicalHistory update(Long id, MedicalHistory medicalHistory) {
        MedicalHistory existingMedicalHistory = findById(id);
        existingMedicalHistory.setDateOfVisit(medicalHistory.getDateOfVisit());
        existingMedicalHistory.setReasonForVisit(medicalHistory.getReasonForVisit());
        existingMedicalHistory.setCurrentMedications(medicalHistory.getCurrentMedications());
        existingMedicalHistory.setKnownAllergies(medicalHistory.getKnownAllergies());
        existingMedicalHistory.setPreviousSurgeriesOrIllness(medicalHistory.getPreviousSurgeriesOrIllness());
        existingMedicalHistory.setVeterinarianObservations(medicalHistory.getVeterinarianObservations());
        existingMedicalHistory.setTreatmentPlanAndRecommendations(medicalHistory.getTreatmentPlanAndRecommendations());
        return medicalHistoryRepository.save(existingMedicalHistory);
    }

    @Override
    public void delete(Long id) {
        MedicalHistory medicalHistory = findById(id);
        medicalHistoryRepository.delete(medicalHistory);
    }
}
