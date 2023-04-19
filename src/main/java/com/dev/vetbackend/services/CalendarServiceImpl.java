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

import java.util.List;

@Service
@AllArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private final CalendarRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;


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
//    public Appointment save(Appointment newAppointment) throws CustomException{
    public Appointment save(Appointment newAppointment) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
//        SubscriptionPlan plan = SubscriptionPlan.fromPlanId(user.getPlanId());
//
//        int currentAppointmentCount = 0;
//        if (plan != SubscriptionPlan.PREMIUM) {
//            currentAppointmentCount = repository.countByUser(user);
//        }
//        if (currentAppointmentCount >= plan.getMaxAppointmentsAllowed()) {
//            throw new CustomException("You have reached the maximum number of appointments allowed for your subscription plan.");
//        }

        newAppointment.setUser(user);
        Appointment appointment = repository.save(newAppointment);
        return appointment;
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
