package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Appointment;
import com.dev.vetbackend.exception.CustomException;

import java.util.List;

public interface CalendarService {
    List<Appointment> findAll();

    List<Appointment> findAllByUser();

    Appointment save(Appointment newAppointment);

    Appointment findById(Long id);

    Appointment update(Long id, Appointment newAppointment);

    void deleteById(Long id);
}
