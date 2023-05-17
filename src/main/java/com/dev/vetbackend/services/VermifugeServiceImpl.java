package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vaccine;
import com.dev.vetbackend.entity.Vermifuge;
import com.dev.vetbackend.repository.VermifugeRepository;
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
public class VermifugeServiceImpl implements VermifugeService {

    @Autowired
    private final VermifugeRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Vermifuge> findAllByUser(Pageable pageable, Long id, String name, String manufacturer) {
        User user = userDetailServiceImpl.getAuthenticatedUser();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vermifuge> cq = cb.createQuery(Vermifuge.class);

        Root<Vermifuge> vermifuge = cq.from(Vermifuge.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(vermifuge.get("user"), user));

        if (id != null) {
            predicates.add(cb.equal(vermifuge.get("id"), id));
        }

        if (name != null) {
            predicates.add(cb.like(vermifuge.get("name"), "%" + name + "%"));
        }

        if (manufacturer != null) {
            predicates.add(cb.like(vermifuge.get("manufacturer"), "%" + manufacturer + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Vermifuge> vermifuges = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return vermifuges.stream()
                .map(vermifugeItem -> {
                    vermifugeItem.setUser(null);
                    return vermifugeItem;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Vermifuge save(Vermifuge newVermifuge) {
        newVermifuge.setUser(userDetailServiceImpl.getAuthenticatedUser());
        Vermifuge vermifuge = repository.save(newVermifuge);
        vermifuge.setUser(null);

        return vermifuge;
    }
    @Override
    public void deleteById(Long id) {
        repository.deleteById(Math.toIntExact(id));
    }
}
