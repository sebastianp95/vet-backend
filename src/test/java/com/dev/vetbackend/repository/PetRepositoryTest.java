package com.dev.vetbackend.repository;
import static org.assertj.core.api.Assertions.assertThat;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
public class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        // given
        User user = new User(/* your user parameters here */);
        userRepository.save(user);

        Pet pet = new Pet(/* your pet parameters here */);
        pet.setUser(user);
        petRepository.save(pet);

        // when
        Optional<Pet> found = petRepository.findById(pet.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(user);
    }

    @Test
    public void testFindAllByUser() {
        // given
        User user = new User(/* your user parameters here */);
        userRepository.save(user);

        Pet pet1 = new Pet(/* your pet parameters here */);
        Pet pet2 = new Pet(/* your pet parameters here */);
        pet1.setUser(user);
        pet2.setUser(user);
        petRepository.saveAll(Arrays.asList(pet1, pet2));

        // when
        Page<Pet> pets = petRepository.findAllByUser(user, PageRequest.of(0, 10));

        // then
        assertThat(pets.getContent()).hasSize(2);
        assertThat(pets.getContent()).contains(pet1, pet2);
    }

    @Test
    public void testCountByUser() {
        // given
        User user = new User(/* your user parameters here */);
        userRepository.save(user);

        Pet pet1 = new Pet(/* your pet parameters here */);
        Pet pet2 = new Pet(/* your pet parameters here */);
        pet1.setUser(user);
        pet2.setUser(user);
        petRepository.saveAll(Arrays.asList(pet1, pet2));

        // when
        int count = petRepository.countByUser(user);

        // then
        assertThat(count).isEqualTo(2);
    }
}
