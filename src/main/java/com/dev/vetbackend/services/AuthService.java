package com.dev.vetbackend.services;

import com.dev.vetbackend.dto.AuthCredentialsDTO;

public interface AuthService {
    String authenticate(AuthCredentialsDTO authCredentials);
    // Other authentication-related methods
}
