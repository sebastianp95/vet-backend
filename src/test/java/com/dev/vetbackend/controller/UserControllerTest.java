package com.dev.vetbackend.controller;


import com.dev.vetbackend.dto.AuthCredentialsDTO;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.security.JWTAuthorizationFilter;
import com.dev.vetbackend.security.PasswordConfig;
import com.dev.vetbackend.security.SecurityConfig;
import com.dev.vetbackend.security.TokenUtils;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import com.dev.vetbackend.services.AuthService;
import com.dev.vetbackend.services.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//TODO: Write Documentation for our Integration and Unit testing approach
//@WebMvcTest(UserController.class)
//@Import({ TokenUtils.class, JWTAuthorizationFilter.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailServiceImpl userDetailService;
    @MockBean
    private AuthService authService;
    @MockBean
    private EmailSenderService emailSenderService;

    @Test
    public void testRegister() throws Exception {
        User registrationRequest = new User();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password");
        registrationRequest.setName("Test User");

        mockMvc.perform(post("/api/register")
                        .param("lng", "en")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());


        verify(userDetailService, times(1))
                .registerNewUser(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void registerWhenServiceThrowsExceptionThenBadRequest() throws Exception {
        User registrationRequest = new User();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password");
        registrationRequest.setName("Test User");

        // Mocking the exception thrown by the userDetailService
        String errorMessage = "User already exists";
        Mockito.doThrow(new RuntimeException(errorMessage))
                .when(userDetailService)
                .registerNewUser(registrationRequest.getEmail(), registrationRequest.getPassword(), registrationRequest.getName(), "en");

        mockMvc.perform(post("/api/register")
                        .param("lng", "en")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(userDetailService, times(1))
                .registerNewUser(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testLogin() throws Exception {
        AuthCredentialsDTO authCredentials = new AuthCredentialsDTO();
        authCredentials.setEmail("test@gmail.com");
        authCredentials.setPassword("password");

        when(authService.authenticate(any(AuthCredentialsDTO.class))).thenReturn("mockToken");

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authCredentials)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "mockToken"));

        verify(authService, times(1)).authenticate(any(AuthCredentialsDTO.class));
    }

}