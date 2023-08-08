package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findById(Long id);

    Page<Pet> findAllByUser(User user, Pageable pageable);

    int countByUser(User user);
}
