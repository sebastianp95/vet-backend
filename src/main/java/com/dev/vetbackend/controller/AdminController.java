package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.ErrorResponse;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Value("${secret.admin.key}")
    private String secretToken;

    private final UserDetailServiceImpl userDetailService;

    @Autowired
    public AdminController(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader("Admin_Authorization") String receivedToken) {

        if (!secretToken.equals(receivedToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            userDetailService.deleteUser(id);
            return ResponseEntity.ok().body("User and related data deleted successfully.");
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


}
