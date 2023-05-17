package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vaccine;
import com.dev.vetbackend.repository.VaccineRepository;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VaccineServiceImpl implements VaccineService {

    @Autowired
    private final VaccineRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Vaccine> findAllByUser(Pageable pageable, Long id, String name, String manufacturer) {
        User user = userDetailServiceImpl.getAuthenticatedUser();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vaccine> cq = cb.createQuery(Vaccine.class);

        Root<Vaccine> vaccine = cq.from(Vaccine.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(vaccine.get("user"), user));

        if (id != null) {
            predicates.add(cb.equal(vaccine.get("id"), id));
        }
        if (name != null) {
            predicates.add(cb.like(vaccine.get("name"), "%" + name + "%"));
        }
        if (manufacturer != null) {
            predicates.add(cb.like(vaccine.get("manufacturer"), "%" + manufacturer + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Vaccine> vaccines = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return vaccines.stream()
                .map(vaccineItem -> {
                    vaccineItem.setUser(null);
                    return vaccineItem;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Vaccine save(Vaccine newVaccine) {
        newVaccine.setUser(userDetailServiceImpl.getAuthenticatedUser());
        Vaccine vaccine = repository.save(newVaccine);
        vaccine.setUser(null);

        return vaccine;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(Math.toIntExact(id));
    }
}
