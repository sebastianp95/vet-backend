package com.dev.vetbackend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordConfigTest {

    @Test
    public void encoder() {
        String pass = "tati";
//        System.out.println(passwordEncoder().encode(pass));
    }

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
