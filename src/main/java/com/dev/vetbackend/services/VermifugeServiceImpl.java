package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vermifuge;
import com.dev.vetbackend.repository.VermifugeRepository;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VermifugeServiceImpl implements VermifugeService {

    @Autowired
    private final VermifugeRepository repository;
    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;

//    @Override
//    public List<Vermifuge> findAll() {
//        return repository.findAll();
//    }

    @Override
    public List<Vermifuge> findAllByUser() {
        User user = userDetailServiceImpl.getAuthenticatedUser();
        List<Vermifuge> vermifuges = repository.findAllByUser(user).stream()
                .map(vermifuge -> {
                    vermifuge.setUser(null);
                    return vermifuge;
                })
                .collect(Collectors.toList());
        return vermifuges;
    }

    @Override
    public Vermifuge save(Vermifuge newVermifuge) {
        newVermifuge.setUser(userDetailServiceImpl.getAuthenticatedUser());
        Vermifuge vermifuge = repository.save(newVermifuge);
        vermifuge.setUser(null);

        return vermifuge;
    }
    @Override
    public void deleteById(Long id) {
        repository.deleteById(Math.toIntExact(id));
    }
}
