package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Vaccine;

import java.util.List;

public interface VaccineService {

    List<Vaccine> findAllByUser();

    Vaccine save(Vaccine newVaccine);

    void deleteById(Long id);
}
