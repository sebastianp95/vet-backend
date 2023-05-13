package com.dev.vetbackend.services;

import com.dev.vetbackend.constants.SubscriptionPlan;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.PetVaccine;
import com.dev.vetbackend.entity.PetVermifuge;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.exception.PetNotFoundException;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.repository.PetVaccineRepository;
import com.dev.vetbackend.repository.PetVermifugeRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    @Autowired
    private final PetRepository repository;
    @Autowired
    private final PetVaccineRepository petVaccineRepository;

    @Autowired
    private final PetVermifugeRepository petVermifugeRepository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;


    @Override
    public List<Pet> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Pet> findAllByUser(Pageable pageable) {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Pet> pets = repository.findAllByUser(user, pageable).getContent();

        return pets;
    }

    @Override
    public Pet save(Pet newPet) throws CustomException {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        SubscriptionPlan plan = SubscriptionPlan.fromPlanId(user.getPlanId());

        int currentPetCount = 0;
        if (plan != SubscriptionPlan.PREMIUM) {
            currentPetCount = repository.countByUser(user);
        }
        if (currentPetCount >= plan.getMaxPetsAllowed()) {
            throw new CustomException("You have reached the maximum number of pets allowed for your subscription plan.");
        }

        newPet.setUser(user);
        Pet pet = repository.save(newPet);
        return pet;
    }


    @Override
    public Pet findById(Long id) {
        Pet pet = repository.findById(id).orElseThrow(() -> new PetNotFoundException("La mascota no existe"));
        return pet;
    }

    @Override
    public Pet update(Long id, Pet newPet) {
        return repository.findById(id)
                .map(pet -> {
                    pet.setName(newPet.getName());
                    pet.setAge(newPet.getAge());
                    pet.setBreed(newPet.getBreed());
                    pet.setOwnerId(newPet.getOwnerId());
                    pet.setWeight(newPet.getWeight());
                    pet.setImageSrc(newPet.getImageSrc());

                    Pet updatedPet = repository.save(pet);
                    if (updatedPet == null) {
//                        logger.error("Error updating pet with id " + id);
//                        replace for updateExpection
                        throw new PetNotFoundException("Error updating pet with id " + id);
                    }
//                    logger.info("Pet with id " + id + " updated successfully");
                    return updatedPet;
                })
                .orElseThrow(() -> new PetNotFoundException("Pet with id " + id + " not found"));
    }


    @Override
    public void deleteById(Long id) {
        repository.deleteById(Math.toIntExact(id));
    }

    //    PETVACCINE LOGIC
    @Override
    public List<PetVaccine> findVaccinesByPetId(Long id) {

        List<PetVaccine> list = petVaccineRepository.findByPetId(id);

        return list;
    }

    @Override
    public PetVaccine saveVaccinationRecord(PetVaccine newRecord) {

        PetVaccine petVaccine = petVaccineRepository.save(newRecord);

        return petVaccine;
    }

    @Override
    public void deleteVaccinationRecordById(Long petId, Long vaccineId) {
        petVaccineRepository.deletePetVaccineByPetIdAndVaccineId(petId, String.valueOf(vaccineId));
    }

    //    PETVERMIFUGE LOGIC
    @Override
    public List<PetVermifuge> findVermifugesByPetId(Long id) {

        List<PetVermifuge> list = petVermifugeRepository.findByPetId(id);

        return list;
    }

    @Override
    public PetVermifuge saveVermifugeRecord(PetVermifuge newRecord) {

        PetVermifuge petVermifuge = petVermifugeRepository.save(newRecord);

        return petVermifuge;
    }

    @Override
    public void deleteVermifugeRecordById(Long petId, Long vermifugeId) {
        petVermifugeRepository.deletePetVermifugeByPetIdAndVermifugeId(petId, String.valueOf(vermifugeId));
    }
}
