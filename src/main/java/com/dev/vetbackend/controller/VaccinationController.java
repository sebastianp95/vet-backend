package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.PetVaccination;
import com.dev.vetbackend.entity.Vaccination;
import com.dev.vetbackend.services.VaccinationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccination")
@AllArgsConstructor
public class VaccinationController {

    @Autowired
    public final VaccinationService vaccinationService;

    @GetMapping("")
    public ResponseEntity<?> all(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer resultsPerPage,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String type
    ) {
        Pageable pageable = PageRequest.of(page, resultsPerPage, Sort.by(sortBy));
        List<Vaccination> all = vaccinationService.findAllByUser(pageable, id, name, manufacturer, type);
        return ResponseEntity.ok(all);
    }

    @PostMapping("")
    public ResponseEntity<?> createVaccination(@RequestBody Vaccination newVaccination) {
        Vaccination vaccination = vaccinationService.save(newVaccination);
        return ResponseEntity.ok(vaccination);
    }

    @DeleteMapping("/{id}")
    void deleteVaccination(@PathVariable Long id) {
        vaccinationService.deleteById(id);
    }

    //VACCINATION CARD CONTROLLERS
    @GetMapping("/vaccinationCard/{petId}")
    public ResponseEntity<?> vaccinations(@PathVariable Long petId) {
        List<PetVaccination> vaccinationCard = vaccinationService.findVaccinationsByPetId(petId);
        return ResponseEntity.ok(vaccinationCard);
    }

    @PostMapping("/vaccinationCard")
    public void createVaccinationCardRecord(@RequestBody PetVaccination newRecord) {
        PetVaccination record = vaccinationService.saveVaccinationRecord(newRecord);
    }

    @DeleteMapping("/vaccinationCard/{petId}/{vaccinationId}")
    void deleteVaccinationRecord(@PathVariable Long petId, @PathVariable Long vaccinationId) {
        vaccinationService.deleteVaccinationRecordById(petId, vaccinationId);
    }


}
