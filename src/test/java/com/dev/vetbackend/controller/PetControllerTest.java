package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import com.dev.vetbackend.services.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

public class PetControllerTest {

    @Test
    public void testCreatePet() {

        PetService petServiceMock = mock(PetService.class);
        UserDetailServiceImpl userDetailService = mock(UserDetailServiceImpl.class);

        Pet newPet = new Pet();
        newPet.setId(1L);

        User user = new User();
        user.setId(4l);

        when(petServiceMock.save(newPet)).thenReturn(newPet);
        when(userDetailService.getAuthenticatedUser()).thenReturn(user);

        PetController petController = new PetController(petServiceMock);
        ResponseEntity<?> result = petController.createPet(newPet);

        verify(petServiceMock, times(1)).save(newPet);
    }

}
