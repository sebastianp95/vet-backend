package com.dev.vetbackend.controller;

import com.dev.vetbackend.dto.AppointmentDTO;
import com.dev.vetbackend.entity.ErrorResponse;
import com.dev.vetbackend.entity.Appointment;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.services.CalendarService;
import com.dev.vetbackend.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@AllArgsConstructor
public class CalendarController {

    @Autowired
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<?> all() {
        List<Appointment> all = calendarService.findAllByUser();
        return ResponseEntity.ok(all);
    }

    @PostMapping("")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentDTO newAppointmentDTO) {
            Appointment appointment = calendarService.save(newAppointmentDTO);
            return ResponseEntity.ok(appointment);
    }

    @GetMapping("/{id}")
    Appointment one(@PathVariable Long id) {
        return calendarService.findById(id);
    }

    @PutMapping("/{id}")
    Appointment editAppointment(@RequestBody AppointmentDTO newAppointmentDTO, @PathVariable Long id) {
        return calendarService.update(id, newAppointmentDTO);
    }

    @DeleteMapping("/{id}")
    void deleteAppointment(@PathVariable Long id) {
        calendarService.deleteById(id);
    }

}
