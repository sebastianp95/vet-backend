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

        updatePetIfNecessary(pet, medicalHistory);

        medicalHistory.setPet(pet);

        User user = userDetailServiceImpl.getAuthenticatedUser();
        medicalHistory.setUser(user);

        return medicalHistoryRepository.save(medicalHistory);
    }

    private void updatePetIfNecessary(Pet pet, MedicalHistory medicalHistory) {
        boolean shouldUpdatePet = false;

        if (medicalHistory.getWeight() != pet.getWeight()) {
            pet.setWeight(medicalHistory.getWeight());
            shouldUpdatePet = true;
        }
        if (medicalHistory.getAge() != pet.getAge()) {
            pet.setAge(medicalHistory.getAge());
            shouldUpdatePet = true;
        }
        if (shouldUpdatePet) {
            petRepository.save(pet);
        }
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
        existingMedicalHistory.setConductedBy(medicalHistory.getConductedBy());
        existingMedicalHistory.setAnamnesis(medicalHistory.getAnamnesis());
        existingMedicalHistory.setWeight(medicalHistory.getWeight());
        existingMedicalHistory.setAge(medicalHistory.getAge());
        existingMedicalHistory.setTemperature(medicalHistory.getTemperature());
        existingMedicalHistory.setHeartRate(medicalHistory.getHeartRate());
        existingMedicalHistory.setGeneralCondition(medicalHistory.getGeneralCondition());
        existingMedicalHistory.setSkinAndCoat(medicalHistory.getSkinAndCoat());
        existingMedicalHistory.setRespiratorySystem(medicalHistory.getRespiratorySystem());
        existingMedicalHistory.setCardiovascularSystem(medicalHistory.getCardiovascularSystem());
        existingMedicalHistory.setMusculoskeletalSystem(medicalHistory.getMusculoskeletalSystem());
        existingMedicalHistory.setGenitourinary(medicalHistory.getGenitourinary());
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
