package com.dev.vetbackend.services;

import com.dev.vetbackend.dto.PetVaccinationDTO;
import com.dev.vetbackend.entity.*;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.repository.PetVaccinationRepository;
import com.dev.vetbackend.repository.VaccinationRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VaccinationServiceImpl implements VaccinationService {

    @Autowired
    private final VaccinationRepository repository;
    @Autowired
    private final PetVaccinationRepository petVaccinationRepository;
    @Autowired
    private final PetRepository petRepository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Vaccination> findAllByUser(Pageable pageable, Long id, String name, String manufacturer, String type) {
        User user = userDetailServiceImpl.getAuthenticatedUser();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vaccination> cq = cb.createQuery(Vaccination.class);

        Root<Vaccination> vaccination = cq.from(Vaccination.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(vaccination.get("user"), user));

        if (id != null) {
            predicates.add(cb.equal(vaccination.get("id"), id));
        }
        if (name != null) {
            predicates.add(cb.like(vaccination.get("name"), "%" + name + "%"));
        }
        if (manufacturer != null) {
            predicates.add(cb.like(vaccination.get("manufacturer"), "%" + manufacturer + "%"));
        }
        if (type != null) {
            predicates.add(cb.like(vaccination.get("type"), "%" + type + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Vaccination> vaccinations = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return vaccinations.stream()
                .map(vaccinationItem -> {
                    vaccinationItem.setUser(null);
                    return vaccinationItem;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Vaccination save(Vaccination newVaccination) {
        newVaccination.setUser(userDetailServiceImpl.getAuthenticatedUser());
        Vaccination vaccination = repository.save(newVaccination);
        vaccination.setUser(null);

        return vaccination;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    //    VACCINATION CARD
    @Override
    public List<PetVaccinationDTO> findVaccinationsByPetId(Long id) {
        List<PetVaccination> list = petVaccinationRepository.findByPetId(id);
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetVaccinationDTO saveVaccinationRecord(PetVaccinationDTO newRecordDTO) {

        PetVaccination newRecord = convertToEntity(newRecordDTO);
        PetVaccination savedPetVaccination = petVaccinationRepository.save(newRecord);
        return convertToDTO(savedPetVaccination);
    }
    
    @Override
    public void deleteVaccinationRecordById(Long id) {
        Optional<PetVaccination> existingRecord = petVaccinationRepository.findById(id);

        if (existingRecord.isEmpty()) {
            throw new IllegalArgumentException("Record not found");
        }

        petVaccinationRepository.delete(existingRecord.get());
    }

    private PetVaccinationDTO convertToDTO(PetVaccination petVaccination) {
        // Assuming you have a constructor or setter methods to set all the properties for PetVaccinationDTO
        PetVaccinationDTO dto = new PetVaccinationDTO();
        dto.setId(petVaccination.getId());
        dto.setPetId(petVaccination.getPet().getId());
        dto.setVaccinationId(petVaccination.getVaccination().getId());
        dto.setVaccinationName(petVaccination.getVaccinationName());
        dto.setVaccinationType(petVaccination.getVaccinationType());
        dto.setDate(petVaccination.getDate());
        // Add more fields here if you have them
        return dto;
    }

    private PetVaccination convertToEntity(PetVaccinationDTO petVaccinationDTO) {
        PetVaccination petVaccination = new PetVaccination();

        petVaccination.setId(petVaccinationDTO.getId());

        // Fetch the Pet entity by ID
        Pet pet = petRepository.findById(petVaccinationDTO.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
        petVaccination.setPet(pet);

        // Fetch the Vaccination entity by ID
        Vaccination vaccination = repository.findById(petVaccinationDTO.getVaccinationId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccination not found"));
        petVaccination.setVaccination(vaccination);

        petVaccination.setVaccinationName(petVaccinationDTO.getVaccinationName());
        petVaccination.setVaccinationType(petVaccinationDTO.getVaccinationType());
        petVaccination.setDate(petVaccinationDTO.getDate());

        return petVaccination;
    }

}
