package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.PetVermifuge;
import com.dev.vetbackend.entity.PetVermifugeId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetVermifugeRepository extends JpaRepository<PetVermifuge, PetVermifugeId> {
    List<PetVermifuge> findByPetId(Long petId);
    @Transactional
    void deletePetVermifugeByPetIdAndVermifugeId(@Param("petId") Long petId, @Param("vermifugeId") String vermifugeId);

}
