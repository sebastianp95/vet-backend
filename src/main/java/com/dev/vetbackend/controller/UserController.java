package com.dev.vetbackend.controller;


import com.dev.vetbackend.dto.AuthCredentialsDTO;
import com.dev.vetbackend.dto.EmailRequest;
import com.dev.vetbackend.dto.ResetPasswordRequest;
import com.dev.vetbackend.dto.SubscriptionStatusDTO;
import com.dev.vetbackend.dto.UserDTO;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import com.dev.vetbackend.services.AuthService;
import com.dev.vetbackend.services.EmailSenderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailServiceImpl userDetailService;
    private final AuthService authService;
    private final EmailSenderService emailSenderService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam(value = "lng", defaultValue = "en") String language, @RequestBody User registrationRequest) {
        try {
            userDetailService.registerNewUser(registrationRequest.getEmail(), registrationRequest.getPassword(), registrationRequest.getName(), language);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthCredentialsDTO authCredentials, HttpServletResponse response) {
        String token = authService.authenticate(authCredentials);
//      response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("Authorization", token);
        return ResponseEntity.ok().build(); // or return any other appropriate response
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
    public ResponseEntity<?> getUser(Principal principal) {
        try {
            UserDTO userDTO = userDetailService.fetchAuthenticatedUserDTO(principal);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user/subscription-status")
    public ResponseEntity<?> getUserSubscriptionStatus(Principal principal) {
        try {
            SubscriptionStatusDTO subscriptionStatus = userDetailService.fetchUserSubscriptionStatus(principal);
            return ResponseEntity.ok(subscriptionStatus);
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
