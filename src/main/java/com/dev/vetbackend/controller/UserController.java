package com.dev.vetbackend.controller;


import com.dev.vetbackend.dto.EmailRequest;
import com.dev.vetbackend.dto.ResetPasswordRequest;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import com.dev.vetbackend.services.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserDetailServiceImpl userDetailService;
    private final EmailSenderService emailSenderService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User registrationRequest) {
        try {
            userDetailService.registerNewUser(registrationRequest.getEmail(), registrationRequest.getPassword(), registrationRequest.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<?> update(@RequestBody User newUserInfo, @PathVariable Long id) {
        try {
            userDetailService.updateUser(id, newUserInfo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUser() {
        try {
            return ResponseEntity.ok(userDetailService.getAuthenticatedUserDTO());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send_recovery_email")
    public ResponseEntity sendEmail(@RequestBody EmailRequest emailMessage) {
        emailSenderService.sendEmail(emailMessage.getRecipientEmail(), "PetPilot Password Recovery", "Your OTP is: " + emailMessage.getOTP());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/reset_password")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userDetailService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
