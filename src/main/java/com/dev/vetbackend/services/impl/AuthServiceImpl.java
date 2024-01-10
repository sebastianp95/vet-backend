package com.dev.vetbackend.services.impl;

import com.dev.vetbackend.dto.AuthCredentialsDTO;
import com.dev.vetbackend.security.TokenUtils;
import com.dev.vetbackend.security.UserDetailsImpl;
import com.dev.vetbackend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public String authenticate(AuthCredentialsDTO authCredentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authCredentials.getEmail(),
                        authCredentials.getPassword(),
                        Collections.emptyList()
                )
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return tokenUtils.createToken(userDetails.getName(), userDetails.getUsername());
    }

}
