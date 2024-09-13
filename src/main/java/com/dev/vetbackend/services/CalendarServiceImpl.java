package com.dev.vetbackend.services;


import com.dev.vetbackend.constants.Reason;
import com.dev.vetbackend.dto.AppointmentDTO;
import com.dev.vetbackend.entity.Appointment;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.exception.PetNotFoundException;
import com.dev.vetbackend.repository.CalendarRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import jakarta.transaction.Transactional;
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
    @Autowired
    private EmailSenderService emailSenderService;


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
    public Appointment save(AppointmentDTO newAppointmentDTO) {
        if (newAppointmentDTO == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }

        User user = userDetailServiceImpl.getAuthenticatedUser();
        if (user == null) {
            throw new IllegalStateException("User must be authenticated");
        }

        Appointment newAppointment = mapAppointmentDtoToEntity(newAppointmentDTO);
        newAppointment.setUser(user);
        Appointment appointment = repository.save(newAppointment);

        if (appointment == null) {
            throw new CustomException("Error saving appointment");
        }

        // Send notifications in the specified language
        sendNotifications(newAppointmentDTO);
        return appointment;
    }


    private void sendNotifications(AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getPhoneNumber() != null && !appointmentDTO.getPhoneNumber().isEmpty() && appointmentDTO.isSendSMS()) {
            String message = buildMessage(appointmentDTO);
            twilioService.sendMessage(appointmentDTO.getCountryCode()+appointmentDTO.getPhoneNumber(), message);
        }

        if (appointmentDTO.getEmail() != null && !appointmentDTO.getEmail().isEmpty() && appointmentDTO.isSendEmail()) {
            String emailMessage = buildEmailMessage(appointmentDTO);
            emailSenderService.sendHtmlEmail(appointmentDTO.getEmail(), "Your Appointment Confirmation", emailMessage);
        }
    }
    private String buildMessage(AppointmentDTO appointmentDTO) {
        String language = appointmentDTO.getLanguage();
        StringBuilder message = new StringBuilder();

        if ("es".equals(language)) {
            message.append("¡Hola! Solo una nota rápida para confirmar tu cita. ")
                    .append("Está todo listo para ")
                    .append(formatDateTime(appointmentDTO.getDate()))
                    .append(". ")
                    .append("Estamos emocionados de conocer a ")
                    .append(appointmentDTO.getPetName())
                    .append("! ");
        } else {
            message.append("Hello there! Just a quick note to confirm your appointment. ")
                    .append("It's all set for ")
                    .append(formatDateTime(appointmentDTO.getDate()))
                    .append(". ")
                    .append("We're excited to meet ")
                    .append(appointmentDTO.getPetName())
                    .append("! ");
        }

        if (appointmentDTO.getMessage() != null && !appointmentDTO.getMessage().isEmpty()) {
            if ("es".equals(language)) {
                message.append("\n\nP.D. ").append(appointmentDTO.getMessage());
            } else {
                message.append("\n\nP.S. ").append(appointmentDTO.getMessage());
            }
        }

        return message.toString();
    }

    private String buildEmailMessage(AppointmentDTO appointmentDTO) {
        String language = appointmentDTO.getLanguage();
        StringBuilder emailMessage = new StringBuilder();

        emailMessage.append("<html>");
        emailMessage.append("<head><title>");

        if ("es".equals(language)) {
            emailMessage.append("Confirmación de tu cita");
        } else {
            emailMessage.append("Your Appointment Confirmation");
        }

        emailMessage.append("</title></head>");
        emailMessage.append("<body>");
        emailMessage.append("<h1>");

        if ("es".equals(language)) {
            emailMessage.append("Confirmación de tu cita");
        } else {
            emailMessage.append("Your Appointment Confirmation");
        }

        emailMessage.append("</h1>");
        emailMessage.append("<p>");

        if ("es".equals(language)) {
            emailMessage.append("¡Hola! Solo una nota rápida para confirmar tu cita para tu mascota, ");
        } else {
            emailMessage.append("Hello there! Just a quick note to confirm your appointment for your pet, ");
        }

        emailMessage.append(appointmentDTO.getPetName()).append(".</p>");
        emailMessage.append("<p>Date and Time: ").append(formatDateTime(appointmentDTO.getDate())).append("</p>");
        emailMessage.append("<p>Service Type: ").append(appointmentDTO.getReason()).append("</p>");

        if ("es".equals(language)) {
            emailMessage.append("<p>Estamos emocionados de conocer a ").append(appointmentDTO.getPetName()).append("!</p>");
        } else {
            emailMessage.append("<p>We're excited to meet you and ").append(appointmentDTO.getPetName()).append("!</p>");
        }

        if (appointmentDTO.getMessage() != null && !appointmentDTO.getMessage().isEmpty()) {
            if ("es".equals(language)) {
                emailMessage.append("<p><b>Notas adicionales:</b> ").append(appointmentDTO.getMessage()).append("</p>");
            } else {
                emailMessage.append("<p><b>Additional Notes:</b> ").append(appointmentDTO.getMessage()).append("</p>");
            }
        }

        emailMessage.append("</body>");
        emailMessage.append("</html>");

        return emailMessage.toString();
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
    @Transactional
    @Override
    public Appointment update(Long id, AppointmentDTO newAppointmentDTO) {
        return repository.findById(id)
                .map(appointment -> {
                    try {
                        appointment.setPetName(newAppointmentDTO.getPetName());
                        appointment.setReason(Reason.valueOf(newAppointmentDTO.getReason()));
                    } catch (IllegalArgumentException e) {
                        throw new CustomException("Invalid reason provided");
                    }
                    appointment.setCountryCode(newAppointmentDTO.getCountryCode());
                    appointment.setPhoneNumber(newAppointmentDTO.getPhoneNumber());
                    appointment.setDate(newAppointmentDTO.getDate());
                    appointment.setEmail(newAppointmentDTO.getEmail());
                    appointment.setPetOwnerName(newAppointmentDTO.getPetOwnerName());
                    appointment.setNotes(newAppointmentDTO.getNotes());

                    Appointment updatedAppointment = repository.save(appointment);
                    if (updatedAppointment == null) {
                        throw new CustomException("Error updating appointment with id " + id);
                    }
                    sendNotifications(newAppointmentDTO);
                    return updatedAppointment;
                })
                .orElseThrow(() -> new CustomException("Appointment with id " + id + " not found"));
    }



    @Override
    public void deleteById(Long id) {
        repository.deleteById(Math.toIntExact(id));
    }

    private Appointment mapAppointmentDtoToEntity(AppointmentDTO appointmentDTO) {
        if (appointmentDTO == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setPetName(appointmentDTO.getPetName());
        appointment.setReason(Reason.valueOf(appointmentDTO.getReason()));
        appointment.setCountryCode(appointmentDTO.getCountryCode());
        appointment.setPhoneNumber(appointmentDTO.getPhoneNumber());
        appointment.setEmail(appointmentDTO.getEmail());
        appointment.setPetOwnerName(appointmentDTO.getPetOwnerName());
        appointment.setDate(appointmentDTO.getDate());
        appointment.setNotes(appointmentDTO.getNotes());


        return appointment;
    }

}
