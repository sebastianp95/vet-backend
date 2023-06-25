package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.*;
import com.dev.vetbackend.repository.PetVaccinationRepository;
import com.dev.vetbackend.repository.VaccinationRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import jakarta.persistence.EntityManager;
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
    public List<PetVaccination> findVaccinationsByPetId(Long id) {
        List<PetVaccination> list = petVaccinationRepository.findByPetId(id);
        return list;
    }

    @Override
    public PetVaccination saveVaccinationRecord(PetVaccination newRecord) {
        // Validate if vaccinationId exists
        Optional<Vaccination> existingVaccination = repository.findById(Long.valueOf(newRecord.getVaccinationId()));
        if (existingVaccination.isEmpty()) { // Use isEmpty() to check if Optional is empty
            throw new IllegalArgumentException("Vaccination ID not found");
        }
        PetVaccination petVaccination = petVaccinationRepository.save(newRecord);
        return petVaccination;
    }


    @Override
    public void deleteVaccinationRecordById(Long petId, Long vaccinationId) {
        petVaccinationRepository.deletePetVaccinationByPetIdAndVaccinationId(petId, String.valueOf(vaccinationId));
    }
}
