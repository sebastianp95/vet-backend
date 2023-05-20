package com.dev.vetbackend.services;


import com.dev.vetbackend.entity.Appointment;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.exception.PetNotFoundException;
import com.dev.vetbackend.repository.CalendarRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private final CalendarRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private final TwilioService twilioService;


    @Override
    public List<Appointment> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Appointment> findAllByUser() {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Appointment> appointments = repository.findAllByUser(user);

        return appointments;
    }

    @Override
    public Appointment save(Appointment newAppointment) {
        if (newAppointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }

        User user = userDetailServiceImpl.getAuthenticatedUser();
        if (user == null) {
            throw new IllegalStateException("User must be authenticated");
        }

        newAppointment.setUser(user);
        Appointment appointment = repository.save(newAppointment);

        if (appointment == null) {
            throw new CustomException("Error saving appointment");
        }

        if (appointment.getPhoneNumber() != null && !appointment.getPhoneNumber().isEmpty()) {
            String message = buildMessage(appointment);
            twilioService.sendMessage(appointment.getPhoneNumber(), message);
        }

        return appointment;
    }

    private String buildMessage(Appointment appointment) {
        StringBuilder message = new StringBuilder();
        message.append("Hello there! Just a quick note to confirm your appointment. ")
                .append("It's all set for ")
                .append(formatDateTime(appointment.getDate()))
                .append(". ")
                .append("We're excited to meet ")
                .append(appointment.getPetName())
                .append("! ");

        if (appointment.getMessage() != null && !appointment.getMessage().isEmpty()) {
            message.append("\n\nP.S. ").append(appointment.getMessage());
        }

        return message.toString();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm");
        return dateTime.format(formatter);
    }


    @Override
    public Appointment findById(Long id) {
        Appointment appointment = repository.findById(id).orElseThrow(() -> new PetNotFoundException("La mascota no existe"));
        return appointment;
    }

    @Override
    public Appointment update(Long id, Appointment newAppointment) {
        return repository.findById(id)
                .map(appointment -> {
                    appointment.setPetName(newAppointment.getPetName());
                    appointment.setReason(newAppointment.getReason());
                    appointment.setPhoneNumber(newAppointment.getPhoneNumber());
                    appointment.setDate(newAppointment.getDate());

                    Appointment updatedAppointment = repository.save(appointment);
                    if (updatedAppointment == null) {
//                        logger.error("Error updating appointment with id " + id);
//                        replace for updateExpection
                        throw new PetNotFoundException("Error updating appointment with id " + id);
                    }
//                    logger.info("Appointment with id " + id + " updated successfully");
                    return updatedAppointment;
                })
                .orElseThrow(() -> new PetNotFoundException("Appointment with id " + id + " not found"));
    }


    @Override
    public void deleteById(Long id) {
        repository.deleteById(Math.toIntExact(id));
    }


}
