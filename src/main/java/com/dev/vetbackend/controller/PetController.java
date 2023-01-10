package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    List<Pet> all() {
        return petService.findAll();
    }

//    @GetMapping("")
//    public ResponseEntity<?> all(@AuthenticationPrincipal User user) {
//        List<Pet> all = petService.findAll();
//        return ResponseEntity.ok(all);
//    }

    @PostMapping("")
    Pet newPet(@RequestBody Pet newPet) {
        return petService.save(newPet);
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
}
