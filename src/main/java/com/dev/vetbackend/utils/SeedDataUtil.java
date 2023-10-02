package com.dev.vetbackend.utils;

import com.dev.vetbackend.constants.ProductType;
import com.dev.vetbackend.constants.ReproductiveStatus;
import com.dev.vetbackend.constants.Species;
import com.dev.vetbackend.entity.MedicalHistory;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vaccination;
import com.dev.vetbackend.repository.MedicalHistoryRepository;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SeedDataUtil {
    private PetRepository petRepository;
    private MedicalHistoryRepository medicalHistoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public SeedDataUtil(PetRepository petRepository, MedicalHistoryRepository medicalHistoryRepository, ProductRepository productRepository) {
        this.petRepository = petRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.productRepository = productRepository;
    }

    public void seedDataForNewUser(User user, String lng) {
        Pet newPet = createFirstPet(user, lng);
        createFirstMedicalHistory(newPet, user, lng);
        createFirstVaccines(newPet, user, lng);
    }

    private Pet createFirstPet(User user, String lng) {
        Pet newPet = new Pet();
        newPet.setAge(8);
        newPet.setBreed("Border Collie");
        newPet.setImageSrc("https://res.cloudinary.com/dgghss1d9/image/upload/v1693877886/vetpics/e7usxgokd9nh1hfppyfg.jpg");
        newPet.setName("Otto");
        newPet.setSpecies(Species.DOG);
        newPet.setGender("M");
        newPet.setOwnerName("John Doe");
        newPet.setOwnerEmail("johndoe@example.com");
        newPet.setOwnerId(4046223334L);
        newPet.setReproductiveStatus(ReproductiveStatus.NEUTERED);
        newPet.setUser(user);
        newPet.setWeight(22);
        return petRepository.save(newPet);
    }

    private void createFirstMedicalHistory(Pet pet, User user, String lng) {
        MedicalHistory medicalHistory = new MedicalHistory();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        medicalHistory.setDateOfVisit(formattedDate);
        medicalHistory.setCurrentMedications(Collections.emptyList());
        medicalHistory.setKnownAllergies(Collections.emptyList());
        medicalHistory.setPreviousSurgeriesOrIllness(Collections.emptyList());
        if ("es".equals(lng)) {
            medicalHistory.setReasonForVisit("Chequeo de rutina");
            medicalHistory.setVeterinarianObservations("La mascota está en buen estado de salud");
            medicalHistory.setTreatmentPlanAndRecommendations("Continuar con los medicamentos actuales y programar una cita de seguimiento en 6 meses");
        } else {
            medicalHistory.setReasonForVisit("Routine checkup");
            medicalHistory.setVeterinarianObservations("Pet is in good health");
            medicalHistory.setTreatmentPlanAndRecommendations("Continue with current medications and schedule follow-up appointment in 6 months");
        }
        medicalHistory.setUser(user);
        medicalHistory.setPet(pet);

        medicalHistoryRepository.save(medicalHistory);
    }

    private void createFirstVaccines(Pet pet, User user, String lng) {
        String rabies = "Rabies";
        String distemper = "Distemper";
        String parvovirus = "Parvovirus";
        String bordetella = "Bordetella";
        String drontalPlus = "Drontal Plus";
        String interceptor = "Interceptor";
        String felineHerpesvirus = "Feline Herpesvirus";
        String felineCalicivirus = "Feline Calicivirus";
        String felineLeukemia = "Feline Leukemia";
        String drontalForCats = "Drontal for Cats";
        String milbemax = "Milbemax";

        if ("es".equals(lng)) {
            rabies = "Rabia";
            distemper = "Moquillo";
            parvovirus = "Parvovirus";
            bordetella = "Bordetella";
            drontalPlus = "Drontal Plus";
            interceptor = "Interceptor";
            felineHerpesvirus = "Herpesvirus Felino";
            felineCalicivirus = "Calicivirus Felino";
            felineLeukemia = "Leucemia Felina";
            drontalForCats = "Drontal para Gatos";
            milbemax = "Milbemax";
        }

        List<Vaccination> seedListVaccination = Arrays.asList(
                // Dog Vaccines
                new Vaccination(rabies, 10000L, 5000L, "Zoetis", 6, "Dog", "vaccine", "https://..."),
                new Vaccination(distemper, 12000L, 6000L, "Merck Animal Health", 6, "Dog", "vaccine", null),
                new Vaccination(parvovirus, 9000L, 4500L, "Boehringer Ingelheim", 12, "Dog", "vaccine", null),
                new Vaccination(bordetella, 8000L, 4000L, "Zoetis", 8, "Dog", "vaccine", null),
                // Dog Vermifuges
                new Vaccination(drontalPlus, 5000L, 2500L, "Bayer Animal Health", 10, "Dog", "vermifuge", null),
                new Vaccination(interceptor, 4000L, 2000L, "Elanco", 8, "Dog", "vermifuge", null),
                // Cat Vaccines
                new Vaccination(felineHerpesvirus, 11000L, 5500L, "Zoetis", 10, "Cat", "vaccine", "https://..."),
                new Vaccination(felineCalicivirus, 10000L, 5000L, "Merck Animal Health", 15, "Cat", "vaccine", null),
                new Vaccination(felineLeukemia, 13000L, 6500L, "Boehringer Ingelheim", 7, "Cat", "vaccine", null),
                // Cat Vermifuges
                new Vaccination(drontalForCats, 4500L, 2250L, "Bayer Animal Health", 10, "Cat", "vermifuge", null),
                new Vaccination(milbemax, 4800L, 2400L, "Elanco", 8, "Cat", "vermifuge", null)
        );

        for (Vaccination item : seedListVaccination) {
            item.setUser(user);
            item.setProductType(ProductType.VACCINATION);
            productRepository.save(item);
        }

    }

}
