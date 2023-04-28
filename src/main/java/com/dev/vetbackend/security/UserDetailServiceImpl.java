package com.dev.vetbackend.security;


import com.dev.vetbackend.entity.User;
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
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Value("${stripe.plan.basic}")
    private String BASIC_PLAN_PRODUCT_ID;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
        // save the new user to the repository
        updateUserSubscription(user, BASIC_PLAN_PRODUCT_ID, "basic", "active");

    }

    public void updateUserSubscription(User user, String subscriptionId, String planId, String status) {
        // Update the user's subscription information
        user.setSubscriptionId(subscriptionId);
        user.setPlanId(planId);
        user.setStatus(status);
        // Save the updated user to the database
        userRepository.save(user);
    }
}
