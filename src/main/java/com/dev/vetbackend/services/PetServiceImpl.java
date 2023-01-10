package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.repository.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository repository;

    @Override
    public List<Pet> findAll() {
        return repository.findAll();
    }

    @Override
    public Pet save(Pet newPet) {
        return repository.save(newPet);
    }

    @Override
    public Pet findById(Long id) {
        Pet pet = repository.findById(id).orElseThrow(() -> new RuntimeException());
        return pet;
    }

    @Override
    public Pet update(Long id, Pet newPet) {
        return repository.findById(id)
                .map(pet -> {
                    pet.setName(newPet.getName());
//                    pet.setRole(newPet.getRole());
                    return repository.save(newPet);
                })
                .orElseGet(() -> {
                    newPet.setId(id);
                    return repository.save(newPet);
                });
    }

    @Override
    public void deleteById(Long id) {

    }
}
