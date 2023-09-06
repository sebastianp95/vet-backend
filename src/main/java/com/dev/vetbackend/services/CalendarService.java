package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Appointment;
import com.dev.vetbackend.dto.AppointmentDTO;

import java.util.List;

public interface CalendarService {
    List<Appointment> findAll();

    List<Appointment> findAllByUser();
    Appointment save(AppointmentDTO newAppointmentDTO);

    Appointment findById(Long id);

    Appointment update(Long id, AppointmentDTO newAppointmentDTO);

    void deleteById(Long id);
}
