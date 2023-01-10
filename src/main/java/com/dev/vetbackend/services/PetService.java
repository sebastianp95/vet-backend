package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;

import java.util.List;

public interface PetService {
    List<Pet> findAll();

    Pet save(Pet newPet);

    Pet findById(Long id);

    void deleteById(Long id);

    Pet update(Long id, Pet pet);
}
