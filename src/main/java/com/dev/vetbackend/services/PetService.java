package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetService {
    List<Pet> findAll();

    List<Pet> findAllByUser(Pageable pageable);

    List<Pet> findAllByUser(Pageable pageable, Long id, String name, Long ownerPhone);

    Pet save(Pet newPet);

    Pet findById(Long id);


    void deleteById(Long id);

    Pet update(Long id, Pet pet);

}
