package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.Appointment;
import com.dev.vetbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Appointment, Integer> {
    Optional<Appointment> findById(Long id);

    List<Appointment> findAllByUser(User user);

//    int countByUser(User user);
}
