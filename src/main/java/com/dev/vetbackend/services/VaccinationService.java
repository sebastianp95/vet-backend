package com.dev.vetbackend.services;

import com.dev.vetbackend.dto.PetVaccinationDTO;
import com.dev.vetbackend.entity.PetVaccination;
import com.dev.vetbackend.entity.Vaccination;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VaccinationService {

    List<Vaccination> findAllByUser(Pageable pageable, Long id, String name, String manufacturer, String type);

    Vaccination save(Vaccination newVaccination);

    void deleteById(Long id);

    //    VACCINATION CARD
    PetVaccinationDTO saveVaccinationRecord(PetVaccinationDTO newRecord);

    List<PetVaccinationDTO> findVaccinationsByPetId(Long id);

    void deleteVaccinationRecordById(Long id);

}
