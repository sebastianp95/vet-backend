package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Pet;
import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vaccine;
import com.dev.vetbackend.repository.VaccineRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VaccineServiceImpl implements VaccineService {

    @Autowired
    private final VaccineRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;

//    @Override
//    public List<Vaccine> findAll() {
//        return repository.findAll();
//    }

    @Override
    public List<Vaccine> findAllByUser() {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Vaccine> vaccines = repository.findAllByUser(user).stream()
                .map(vaccine -> {
                    vaccine.setUser(null);
                    return vaccine;
                })
                .collect(Collectors.toList());
        return vaccines;
    }

    @Override
    public Vaccine save(Vaccine newVaccine) {
        newVaccine.setUser(userDetailServiceImpl.getAuthenticatedUser());
        Vaccine vaccine = repository.save(newVaccine);
        vaccine.setUser(null);

        return vaccine;
    }
}
