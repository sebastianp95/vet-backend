package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.ErrorResponse;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.entity.PetVermifuge;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@AllArgsConstructor
public class PetController {

    @Autowired
    private final PetService petService;

    @GetMapping("")
    public ResponseEntity<?> all(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "30") Integer resultsPerPage,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long ownerId
    ) {
        Pageable pageable = PageRequest.of(page, resultsPerPage, Sort.by(sortBy));
        List<Pet> pets = petService.findAllByUser(pageable, id, name, ownerId);
        return ResponseEntity.ok(pets);
    }

    @PostMapping("")
    public ResponseEntity<?> createPet(@RequestBody Pet newPet) {
        try {
            Pet pet = petService.save(newPet);
            return ResponseEntity.ok(pet);
        } catch (CustomException e) {
            // Return the error response as a ResponseEntity
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
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

    //    VERMIFUGE CONTROLLERS
    @GetMapping("/vermifugeCard/{id}")
    public ResponseEntity<?> vermifuge(@PathVariable Long id) {
        List<PetVermifuge> vermifugeCard = petService.findVermifugesByPetId(id);
        return ResponseEntity.ok(vermifugeCard);
    }

    @PostMapping("/vermifugeCard")
    public void createVermifugesCardRecord(@RequestBody PetVermifuge newRecord) {
        PetVermifuge record = petService.saveVermifugeRecord(newRecord);
    }

    @DeleteMapping("/vermifugeCard/{petId}/{vermifugeId}")
    void deleteVermifugeRecord(@PathVariable Long petId, @PathVariable Long vermifugeId) {
        petService.deleteVermifugeRecordById(petId, vermifugeId);
    }


}
