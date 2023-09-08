package com.dev.vetbackend.security;


import com.dev.vetbackend.constants.ProductType;
import com.dev.vetbackend.dto.UserDTO;
import com.dev.vetbackend.entity.*;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.repository.MedicalHistoryRepository;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.repository.ProductRepository;
import com.dev.vetbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private PetRepository petRepository;
    private MedicalHistoryRepository medicalHistoryRepository;
    private ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailServiceImpl(
            UserRepository userRepository,
            PetRepository petRepository,
            MedicalHistoryRepository medicalHistoryRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository
                .findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email: " + email + " no existe"));

        return new UserDetailsImpl(user);

    }

    public UserDTO getAuthenticatedUserDTO() {
        User user = getAuthenticatedUser();
        return mapToDTO(user);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = loadUserByUsername(principal.toString());
//        map UserDetails to User
        User user = null;
        try {
            Field userField = UserDetailsImpl.class.getDeclaredField("user");
            userField.setAccessible(true);
            user = (User) userField.get(userDetails);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void registerNewUser(String email, String password, String name) throws Exception {
        // check if user already exists
        Optional<User> optionalUser = userRepository.findOneByEmail(email);
        if (optionalUser.isPresent()) {
            throw new Exception("User with this email already exists");
        }

        // create a new user object
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        // save the new user , first pet, and first medical history to the database
        updateUserSubscription(user, "no_sub", "no_plan", "inactive");
        Pet created = createFirstPet(user);
        createFirstMedicalHistory(created, user);
        createFirstVaccines(created, user);


    }

    public void updateUserSubscription(User user, String subscriptionId, String planId, String status) {
        // Update the user's subscription information
        user.setSubscriptionId(subscriptionId);
        user.setPlanId(planId);
        user.setStatus(status);
        // Save the updated user to the database
        userRepository.save(user);
    }

    public void resetPassword(String email, String newPassword) throws Exception {
        Optional<User> optionalUser = userRepository.findOneByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new Exception("User with this email does not exist");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User updateUser(Long id, User newUserInfo) {
        return userRepository.findById(Math.toIntExact(id))
                .map(existingUser -> {
                    existingUser.setName(newUserInfo.getName());
                    existingUser.setImageSrc(newUserInfo.getImageSrc());
                    existingUser.setPrintImage(newUserInfo.isPrintImage());
                    existingUser.setSecondEmail(newUserInfo.getSecondEmail());
                    existingUser.setPhone(newUserInfo.getPhone());

                    User updatedUser = userRepository.save(existingUser);
                    if (updatedUser == null) {
                        throw new CustomException("Error updating user with id " + id);
                    }
                    return updatedUser;
                })
                .orElseThrow(() -> new CustomException("User with id " + id + " not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(Math.toIntExact(id));
    }
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setImageSrc(user.getImageSrc());
        dto.setPrintImage(user.isPrintImage());
        dto.setSecondEmail(user.getSecondEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    private Pet createFirstPet(User user) {
        Pet newPet = new Pet();
        newPet.setAge(2);
        newPet.setBreed("Akita");
        newPet.setImageSrc("https://res.cloudinary.com/dgghss1d9/image/upload/v1680189604/vetpics/xln8gv3vjo3yczug8l56.jpg");
        newPet.setName("Otto");
        newPet.setUser(user);
        newPet.setWeight(22);
        newPet.setOwnerId(4046235978L);

        return petRepository.save(newPet);
    }

    private void createFirstMedicalHistory(Pet pet, User user) {
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setDateOfVisit("2023-04-30");
        medicalHistory.setReasonForVisit("Routine checkup");
        medicalHistory.setCurrentMedications(List.of("Heartworm prevention", "Flea and tick prevention"));
        medicalHistory.setKnownAllergies(List.of("None"));
        medicalHistory.setPreviousSurgeriesOrIllness(List.of("None"));
        medicalHistory.setVeterinarianObservations("Pet is in good health");
        medicalHistory.setTreatmentPlanAndRecommendations("Continue with current medications and schedule follow-up appointment in 6 months");
        medicalHistory.setUser(user);
        medicalHistory.setPet(pet);

        medicalHistoryRepository.save(medicalHistory);
    }

    private void createFirstVaccines(Pet pet, User user) {
        // First Vaccine
        Vaccination product1 = new Vaccination();
        product1.setName("Rabies");
        product1.setPrice(10000l);
        product1.setProductType(ProductType.VACCINATION);
        product1.setCost(5000l);
        product1.setUser(user);
        product1.setImageSrc("https://res.cloudinary.com/dgghss1d9/image/upload/v1690403475/vetpics/fw6e7ialbf8hqpir2ggp.jpg");
        product1.setManufacturer("Pfizer");
        product1.setQuantity(6);
        product1.setTargetSpecies("Dog");
        product1.setType("vaccine"); // setting type
        productRepository.save(product1);

        // Second Vaccine
        Vaccination product2 = new Vaccination();
        product2.setName("Distemper");
        product2.setPrice(12000l);
        product2.setProductType(ProductType.VACCINATION);
        product2.setCost(6000l);
        product2.setUser(user);
        product2.setImageSrc("https://res.cloudinary.com/dgghss1d9/image/upload/v1690403475/vetpics/fw6e7ialbf8hqpir2ggp.jpg");
        product2.setManufacturer("Pfizer");
        product2.setQuantity(6);
        product2.setTargetSpecies("Dog");
        product2.setType("vaccine"); // setting type
        productRepository.save(product2);

        // Vermifuge
        Vaccination product3 = new Vaccination();
        product3.setName("Drontal Plus");
        product3.setPrice(5000l);
        product3.setProductType(ProductType.VACCINATION); // Assuming vermifuge is a type of medication
        product3.setCost(2500l);
        product3.setUser(user);
        product3.setImageSrc("https://res.cloudinary.com/dgghss1d9/image/upload/v1690403475/vetpics/fw6e7ialbf8hqpir2ggp.jpg");
        product3.setManufacturer("Bayer");
        product3.setQuantity(10);
        product3.setTargetSpecies("Dog");
        product3.setType("vermifuge"); // setting type
        productRepository.save(product3);
    }

}