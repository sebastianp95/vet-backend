package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@AllArgsConstructor
public class RegistrationController {

    private UserDetailServiceImpl userDetailService;

    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody User registrationRequest) {
        try {
            userDetailService.registerNewUser(registrationRequest.getEmail(), registrationRequest.getPassword(), registrationRequest.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
