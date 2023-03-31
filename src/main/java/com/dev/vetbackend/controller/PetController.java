package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/pets")
@AllArgsConstructor
public class PetController {

    @Autowired
    private final PetService petService;

    @GetMapping("")
    public ResponseEntity<?> all() {
        List<Pet> all = petService.findAllByUser();
        return ResponseEntity.ok(all);
    }

    @PostMapping("")
    public ResponseEntity<?> createPet(@RequestBody Pet newPet) {
        Pet pet = petService.save(newPet);
        return ResponseEntity.ok(pet);
    }

    @GetMapping("/{id}")
    Pet one(@PathVariable Long id) {
        return petService.findById(id);
    }

    @PutMapping("/{id}")
    Pet editPet(@RequestBody Pet newPet, @PathVariable Long id) {
        return petService.update(id, newPet);
    }

    @DeleteMapping("/{id}")
    void deletePet(@PathVariable Long id) {
        petService.deleteById(id);
    }

//    VACCINATION CONTROLLERS
    @GetMapping("/vaccineCard/{id}")
    public ResponseEntity<?> vaccines(@PathVariable Long id) {
        List<PetVaccine> vaccinationCard = petService.findVaccinesByPetId(id);
        return ResponseEntity.ok(vaccinationCard);
    }

    @PostMapping("/vaccineCard")
    public void createVaccinesCardRecord(@RequestBody PetVaccine newRecord) {
        PetVaccine record = petService.saveVaccinationRecord(newRecord);
    }

    @DeleteMapping("/vaccineCard/{petId}/{vaccineId}")
    void deleteVaccinationRecord(@PathVariable Long petId, @PathVariable Long vaccineId) {
        petService.deleteVaccinationRecordById(petId, vaccineId);
    }


}
