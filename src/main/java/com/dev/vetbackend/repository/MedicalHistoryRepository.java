package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.MedicalHistory;
import com.dev.vetbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {

    List<MedicalHistory>  findAllByUser(User user);

    List<MedicalHistory> findAllByUserAndPetId(User user, Long id);
}
