package com.dev.vetbackend.repository;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.entity.Vermifuge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VermifugeRepository extends JpaRepository<Vermifuge, Integer> {

    List<Vermifuge> findAllByUser(User user);

    Vermifuge save(Vermifuge vermifuge);


}
