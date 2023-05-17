package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Vaccine;
import com.dev.vetbackend.services.VaccineService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines")
@AllArgsConstructor
public class VaccineController {

    @Autowired
    public final VaccineService vaccineService;

    @GetMapping("")
    public ResponseEntity<?> all(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer resultsPerPage,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String manufacturer
    ) {
        Pageable pageable = PageRequest.of(page, resultsPerPage, Sort.by(sortBy));
        List<Vaccine> all = vaccineService.findAllByUser(pageable, id, name, manufacturer);
        return ResponseEntity.ok(all);
    }

    @PostMapping("")
    public ResponseEntity<?> createVaccine(@RequestBody Vaccine newVaccine) {
        Vaccine vaccine = vaccineService.save(newVaccine);
        return ResponseEntity.ok(vaccine);
    }

    @DeleteMapping("/{id}")
    void deleteVaccine(@PathVariable Long id) {
        vaccineService.deleteById(id);
    }

}
