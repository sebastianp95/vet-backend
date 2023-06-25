package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findAllByUser(User user);
    Vaccination save(Vaccination vaccination);
}
