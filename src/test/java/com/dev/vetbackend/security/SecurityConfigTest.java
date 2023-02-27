package com.dev.vetbackend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

public class SecurityConfigTest {

    @Test
    public void encoder() {
        String pass = "tati";
        System.out.println(passwordEncoder().encode(pass));
    }

    @Test
    void test() {
        System.out.println(solution(10, 21));
        System.out.println(solution(13, 11));
        System.out.println(solution(2, 1));
        System.out.println(solution(1, 8));
    }

    public int solution(int A, int B) {
        int maxLength = Math.max(A, B); // the maximum length of a stick
        int minLength = 1; // the minimum length of a stick

        return IntStream.rangeClosed(minLength, maxLength)
                .filter(side -> canMakeSquare(A, B, side))
                .max() // get the largest side length
                .orElse(0); // return 0 if no square is possible
    }

    // check if it's possible to make a square of a given side length
    private boolean canMakeSquare(int A, int B, int side) {
        int sticksForSide = 4;
        int sticksForShorter = (A / side) + (B / side);

        return sticksForShorter >= sticksForSide; // return true if we have enough sticks
    }

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
