package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.exception.PetNotFoundException;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.repository.PetVaccineRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    @Autowired
    private final PetRepository repository;
    @Autowired
    private final PetVaccineRepository petVaccineRepository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;


    @Override
    public List<Pet> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Pet> findAllByUser() {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Pet> pets = repository.findAllByUser(user);

        return pets;
    }

    @Override
    public Pet save(Pet newPet) {
        newPet.setUser(userDetailServiceImpl.getAuthenticatedUser());
        Pet pet = repository.save(newPet);

        return pet;
    }

    @Override
    public Pet findById(Long id) {
        Pet pet = repository.findById(id).orElseThrow(() -> new PetNotFoundException("La mascota no existe"));
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

    //    PetVaccine logic
    @Override
    public List<PetVaccine> findVaccinesByPetId(Long id) {

        List<PetVaccine> list = petVaccineRepository.findByPetId(id);

        return list;
    }

    @Override
    public PetVaccine saveVaccinationRecord(PetVaccine newRecord) {

        PetVaccine petVaccine = petVaccineRepository.save(newRecord);

        return petVaccine;
    }
}
