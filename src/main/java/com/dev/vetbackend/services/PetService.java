package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.entity.PetVermifuge;

import java.util.List;

public interface PetService {
    List<Pet> findAll();

    List<Pet> findAllByUser();

    Pet save(Pet newPet);

    Pet findById(Long id);


    void deleteById(Long id);

    Pet update(Long id, Pet pet);

//    VACCINE
    PetVaccine saveVaccinationRecord(PetVaccine newRecord);

    List<PetVaccine> findVaccinesByPetId(Long id);

    void deleteVaccinationRecordById(Long petId, Long vaccineId);

//    VERMIFUGE

    PetVermifuge saveVermifugeRecord(PetVermifuge newRecord);

    List<PetVermifuge> findVermifugesByPetId(Long id);

    void deleteVermifugeRecordById(Long petId, Long vermifugeId);


}
