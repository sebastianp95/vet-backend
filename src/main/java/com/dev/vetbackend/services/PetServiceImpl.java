package com.dev.vetbackend.services;

import com.dev.vetbackend.constants.SubscriptionPlan;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.exception.PetNotFoundException;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    @Autowired
    private final PetRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private EntityManager entityManager;


    @Override
    public List<Pet> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Pet> findAllByUser(Pageable pageable) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Pet> pets = repository.findAllByUser(user, pageable).getContent();


        return pets;
    }

    @Override
    public List<Pet> findAllByUser(Pageable pageable, Long id, String name, Long ownerPhone) {
        User user = userDetailServiceImpl.getAuthenticatedUser();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);

        Root<Pet> pet = cq.from(Pet.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(pet.get("user"), user));

        if (id != null) {
            predicates.add(cb.equal(pet.get("id"), id));
        }

        if (name != null) {
            predicates.add(cb.like(pet.get("name"), "%" + name + "%"));
        }

        if (ownerPhone != null) {
            predicates.add(cb.like(pet.get("ownerId").as(String.class), "%" + ownerPhone + "%"));
        }


        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();
    }

    @Override
    public Pet save(Pet newPet) throws CustomException {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        SubscriptionPlan plan = SubscriptionPlan.fromPlanId(user.getPlanId());

        int currentPetCount = 0;
        if (plan != SubscriptionPlan.PREMIUM && plan != SubscriptionPlan.PLUS) {
            currentPetCount = repository.countByUser(user);
        }
        if (currentPetCount >= plan.getMaxPetsAllowed()) {
            throw new CustomException("You have reached the maximum number of pets allowed for your subscription plan.");
        }

        newPet.setUser(user);
        Pet pet = repository.save(newPet);
        return pet;
    }


    @Override
    public Pet findById(Long id) {
        Pet pet = repository.findById(id).orElseThrow(() -> new PetNotFoundException("La mascota no existe"));
        return pet;
    }

    @Override
    public Pet update(Long id, Pet newPet) {
        return repository.findById(id)
                .map(pet -> {
                    pet.setName(newPet.getName());
                    pet.setSpecies(newPet.getSpecies());
                    pet.setAge(newPet.getAge());
                    pet.setGender(newPet.getGender());
                    pet.setBreed(newPet.getBreed());
                    pet.setReproductiveStatus(newPet.getReproductiveStatus());
                    pet.setOwnerId(newPet.getOwnerId());
                    pet.setOwnerName(newPet.getOwnerName());
                    pet.setOwnerEmail(newPet.getOwnerEmail());
                    pet.setWeight(newPet.getWeight());
                    pet.setImageSrc(newPet.getImageSrc());

                    Pet updatedPet = repository.save(pet);
//                    logger.info("Pet with id " + id + " updated successfully");
                    return updatedPet;
                })
                .orElseThrow(() -> new PetNotFoundException("Pet with id " + id + " not found"));
    }


    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(Math.toIntExact(id));
        } catch (EmptyResultDataAccessException e) {
            throw new PetNotFoundException("Pet not found");
        }
    }


}
