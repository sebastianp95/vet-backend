package com.dev.vetbackend.security;


import com.dev.vetbackend.constants.ProductType;
import com.dev.vetbackend.dto.UserDTO;
import com.dev.vetbackend.entity.*;
import com.dev.vetbackend.exception.CustomException;
import com.dev.vetbackend.repository.MedicalHistoryRepository;
import com.dev.vetbackend.repository.PetRepository;
import com.dev.vetbackend.repository.ProductRepository;
import com.dev.vetbackend.repository.UserRepository;
import com.dev.vetbackend.utils.SeedDataUtil;
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
    private PasswordEncoder passwordEncoder;
    private SeedDataUtil seedDataUtil;

    @Autowired
    public UserDetailServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SeedDataUtil seedDataUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.seedDataUtil = seedDataUtil;
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

    public void registerNewUser(String email, String password, String name, String language) throws Exception {
        Optional<User> optionalUser = userRepository.findOneByEmail(email);
        if (optionalUser.isPresent()) {
            throw new Exception("User with this email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        // save the new user , first pet, and first medical history to the database
        updateUserSubscription(user, "no_sub", "no_plan", "inactive");
        seedDataUtil.seedDataForNewUser(user, language);

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

}