package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Vaccine;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VaccineService {

    List<Vaccine> findAllByUser(Pageable pageable, Long id, String name, String manufacturer);

    Vaccine save(Vaccine newVaccine);

    void deleteById(Long id);
}
