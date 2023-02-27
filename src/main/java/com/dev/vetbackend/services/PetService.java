package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;

import java.util.List;

public interface PetService {
    List<Pet> findAll();

    List<Pet> findAllByUser();

    Pet save(Pet newPet);

    Pet findById(Long id);


    void deleteById(Long id);

    Pet update(Long id, Pet pet);
    PetVaccine saveVaccinationRecord(PetVaccine newRecord);

    List<PetVaccine> findVaccinesByPetId(Long id);
}
