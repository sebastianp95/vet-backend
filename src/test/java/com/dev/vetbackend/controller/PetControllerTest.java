package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dev.vetbackend.services.PetService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Test
    @WithMockUser
    public void testAllPetsWhenNoPetsAvailable() throws Exception {
        when(petService.findAllByUser(any(Pageable.class), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser
    public void testAllPetsWhenMultiplePetsAvailable() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Buddy");

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Lucy");

        List<Pet> pets = Arrays.asList(pet1, pet2);
        when(petService.findAllByUser(any(Pageable.class), any(), any(), any())).thenReturn(pets);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].name", equalTo("Buddy")))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[1].name", equalTo("Lucy")));
    }



    @Test
    @WithMockUser
    public void testGetSinglePet() throws Exception {
        Long petId = 1L;
        Pet pet = new Pet(); // Populate the pet object
        when(petService.findById(petId)).thenReturn(pet);

        mockMvc.perform(get("/api/pets/" + petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(pet.getId())));
    }

    @Test
    @WithMockUser
    public void testCreatePet() throws Exception {
        Pet newPet = new Pet();
        newPet.setName("Buddy");
        newPet.setOwnerId("12"); // Set other necessary properties

        Pet savedPet = new Pet();
        savedPet.setId(1L); // Simulate the pet being saved with an ID
        savedPet.setName(newPet.getName());
        savedPet.setOwnerId(newPet.getOwnerId());

        when(petService.save(any(Pet.class))).thenReturn(savedPet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newPet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Buddy")));

        verify(petService, times(1)).save(any(Pet.class));
    }
}

