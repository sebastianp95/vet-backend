package com.dev.vetbackend.security;


import com.dev.vetbackend.entity.MedicalHistory;
import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.repository.MedicalHistoryRepository;
import com.dev.vetbackend.repository.PetRepository;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailServiceImpl(
            UserRepository userRepository,
            PetRepository petRepository,
            MedicalHistoryRepository medicalHistoryRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository
                .findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email: " + email + " no existe"));

        return new UserDetailsImpl(user);

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
        updateUserSubscription(user, "no_sub", "basic", "active");
        Pet created = createFirstPet(user);
        createFirstMedicalHistory(created, user);


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


    public Pet createFirstPet(User user) {
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


    public void createFirstMedicalHistory(Pet pet, User user) {
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

}
