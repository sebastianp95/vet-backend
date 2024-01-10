package com.dev.vetbackend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TokenUtilsTest {

    @Autowired
    private TokenUtils tokenUtils;

    @Test
    public void testCreateToken() {
        String name = "Test User";
        String email = "test@example.com";

        String token = tokenUtils.createToken(name, email);

        assertNotNull(token);
        // Additional assertions can be made to verify the structure and contents of the token
    }

    @Test
    public void testGetAuthentication() {
        String name = "Test User";
        String email = "test@example.com";

        String token = tokenUtils.createToken(name, email);

        UsernamePasswordAuthenticationToken authentication = tokenUtils.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(email, authentication.getName());
        // Additional assertions to verify the authorities and details
    }

    @Test
    public void testGetAuthenticationWithInvalidToken() {
        String invalidToken = "invalid.token";

        UsernamePasswordAuthenticationToken authentication = tokenUtils.getAuthentication(invalidToken);

        assertNull(authentication);
    }
}
