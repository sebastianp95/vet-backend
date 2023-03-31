package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Vermifuge;
import com.dev.vetbackend.services.VermifugeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vermifuge")
@AllArgsConstructor
public class VermifugeController {

    @Autowired
    public final VermifugeService vermifugeService;

    @GetMapping("")
    public ResponseEntity<?> all() {
        List<Vermifuge> all = vermifugeService.findAllByUser();
        return ResponseEntity.ok(all);
    }

    @PostMapping("")
    public ResponseEntity<?> createVermifuge(@RequestBody Vermifuge newVermifuge) {
        Vermifuge vermifuge = vermifugeService.save(newVermifuge);
        return ResponseEntity.ok(vermifuge);
    }

    @DeleteMapping("/{id}")
    void deleteVermifuge(@PathVariable Long id) {
        vermifugeService.deleteById(id);
    }

}
