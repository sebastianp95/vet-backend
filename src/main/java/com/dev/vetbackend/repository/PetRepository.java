package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findById(Long id);

    List<Pet> findAllByUser(User user);
}
