package com.dev.vetbackend.services;

import com.dev.vetbackend.constants.SubscriptionPlan;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.exception.PetNotFoundException;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository repository;
    @Mock
    private UserDetailServiceImpl userDetailServiceImpl;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PetServiceImpl underTest;

    private User user;
    private Pet pet;

    private void commonSetup() {
        user = new User();
        user.setPlanId("no_plan");
        pet = new Pet();
        when(userDetailServiceImpl.getAuthenticatedUser()).thenReturn(user);
    }

    @Test
    void findAll() {
        //given
        underTest.findAll();
        //then
        verify(repository).findAll();
    }

    @Test
    void findAllByUser() {
        commonSetup();

        List<Pet> expectedPets = Collections.singletonList(pet);
        Page<Pet> pageOfPets = new PageImpl<>(expectedPets);
        Pageable pageableRequest = mock(Pageable.class);

        when(repository.findAllByUser(user, pageableRequest)).thenReturn(pageOfPets);

        // when
        List<Pet> actualPets = underTest.findAllByUser(pageableRequest);

        // then
        assertEquals(expectedPets, actualPets);
        verify(userDetailServiceImpl).getAuthenticatedUser();
        verify(repository).findAllByUser(user, pageableRequest);
    }

    @Test
    void testFindAllByUserWithFilters() {
        commonSetup();

        TypedQuery<Pet> typedQuery = mock(TypedQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Pet> cq = mock(CriteriaQuery.class);
        Root<Pet> petRoot = mock(Root.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Pet.class)).thenReturn(cq);
        when(cq.from(Pet.class)).thenReturn(petRoot);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);

        Pageable pageable = PageRequest.of(0, 10);
        Long id = 1L;
        String name = "Fido";
        Long ownerPhone = null;

        when(typedQuery.setFirstResult((int) pageable.getOffset())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(pageable.getPageSize())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(new ArrayList<>());

        underTest.findAllByUser(pageable, id, name, ownerPhone);

        // Verifications
        verify(cb).equal(petRoot.get("user"), user);
        verify(cb).equal(petRoot.get("id"), id);
        verify(cb).like(petRoot.get("name"), "%" + name + "%");
        // No verification for ownerPhone as it is null
    }

// TODO:   solve enum isue
    @Test
    @Disabled
    void testSaveWithinPlanLimit() throws CustomException {
        commonSetup();

        // Set the user's plan ID to BASIC
        user.setPlanId(SubscriptionPlan.BASIC.getPlanId());

        // Return the maximum number of pets allowed for the BASIC plan
        when(repository.countByUser(user)).thenReturn(SubscriptionPlan.BASIC.getMaxPetsAllowed());

        when(repository.countByUser(user)).thenReturn(1); // Count is below limit
        when(repository.save(any())).thenReturn(pet);

        Pet savedPet = underTest.save(new Pet());

        assertEquals(pet, savedPet);
    }


    @Test
    void testSaveExceedingPlanLimit() throws CustomException {
        commonSetup();
        user.setPlanId(SubscriptionPlan.BASIC.getPlanId());
        // Return the maximum number of pets allowed for the BASIC plan
        when(repository.countByUser(user)).thenReturn(SubscriptionPlan.BASIC.getMaxPetsAllowed());

        assertThrows(CustomException.class, () -> underTest.save(pet));
    }

    @Test
    void testFindByIdExistingPet() {
        // Arrange
        Long petId = 1L;
        Pet mockPet = new Pet();
        mockPet.setId(petId);
        mockPet.setName("Buddy");

        when(repository.findById(petId)).thenReturn(Optional.of(mockPet));

        // Act
        Pet result = underTest.findById(petId);

        // Assert
        assertNotNull(result);
        assertEquals(mockPet.getId(), result.getId());
        assertEquals(mockPet.getName(), result.getName());
    }

    @Test
    void testFindByIdNonExistingPet() {
        // given
        Long petId = 2L;
        when(repository.findById(petId)).thenReturn(Optional.empty());

        // then
        assertThrows(PetNotFoundException.class, () -> underTest.findById(petId));
    }

    @Test
    void testUpdateExistingPet() {
        // Arrange
        Long petId = 1L;
        Pet existingPet = new Pet();
        existingPet.setId(petId);
        existingPet.setName("Buddy");

        Pet newPetData = new Pet();
        newPetData.setName("Updated Name");

        when(repository.findById(petId)).thenReturn(Optional.of(existingPet));
        when(repository.save(any(Pet.class))).thenReturn(existingPet);

        // Act
        Pet updatedPet = underTest.update(petId, newPetData);

        // Assert
        assertNotNull(updatedPet);
        assertEquals(newPetData.getName(), updatedPet.getName());
        assertEquals(existingPet.getSpecies(), updatedPet.getSpecies()); // Check other fields too
    }

    @Test
    void testUpdateNonExistingPet() {
        // Arrange
        Long petId = 2L;
        Pet newPetData = new Pet();

        when(repository.findById(petId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PetNotFoundException.class, () -> underTest.update(petId, newPetData));
    }

    @Test
    void testDeleteByIdExistingPet() {
        Long petId = 1L;
        underTest.deleteById(petId);
        verify(repository, times(1)).deleteById(Math.toIntExact(petId));
    }

    @Test
    void testDeleteByIdNonExistingPet() {
        // Arrange
        Long petId = 2L;
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(Math.toIntExact(petId));
        // Act & Assert
        assertThrows(PetNotFoundException.class, () -> underTest.deleteById(petId));
    }
}
