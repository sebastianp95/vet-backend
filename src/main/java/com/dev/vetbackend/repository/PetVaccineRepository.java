package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.entity.PetVaccineId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetVaccineRepository extends JpaRepository<PetVaccine, PetVaccineId> {
    List<PetVaccine> findByPetId(Long petId);
    @Transactional
    void deletePetVaccineByPetIdAndVaccineId(@Param("petId") Long petId, @Param("vaccineId") String vaccineId);

}
