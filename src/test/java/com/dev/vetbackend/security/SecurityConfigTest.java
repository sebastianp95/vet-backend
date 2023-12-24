package com.dev.vetbackend.security;

import com.dev.vetbackend.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailServiceImpl userDetailService;

    @Test
    void rootWhenUnauthenticatedThen403() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isForbidden());
    }
    @Test
    public void accessProtectedEndpointWithInvalidJwtToken() throws Exception {
        mockMvc.perform(get("/api/user")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        mockMvc.perform(get("/api/user")).andExpect(status().isOk());
    }

    @Test
    public void accessPublicEndpoints() throws Exception {

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



}

