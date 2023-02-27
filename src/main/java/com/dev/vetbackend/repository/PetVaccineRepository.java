package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.entity.PetVaccineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetVaccineRepository extends JpaRepository<PetVaccine, PetVaccineId> {
    List<PetVaccine> findByPetId(Long petId);

}
