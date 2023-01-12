package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.Vaccine;
import com.dev.vetbackend.services.VaccineService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines")
@AllArgsConstructor
public class VaccineController {

    @Autowired
    public final VaccineService vaccineService;

    @GetMapping("")
    public ResponseEntity<?> all() {
        List<Vaccine> all = vaccineService.findAllByUser();
        return ResponseEntity.ok(all);
    }

    @PostMapping("")
    public ResponseEntity<?> createVaccine(@RequestBody Vaccine newVaccine) {
        Vaccine vaccine = vaccineService.save(newVaccine);
        return ResponseEntity.ok(vaccine);
    }

}
