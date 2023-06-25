package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.PetVaccination;
import com.dev.vetbackend.entity.PetVaccinationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetVaccinationRepository extends JpaRepository<PetVaccination, PetVaccinationId> {
    List<PetVaccination> findByPetId(Long petId);

    @Transactional
    void deletePetVaccinationByPetIdAndVaccinationId(@Param("petId") Long petId, @Param("vaccinationId") String vaccinationId);
}
