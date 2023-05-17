package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Vermifuge;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VermifugeService {


    List<Vermifuge> findAllByUser(Pageable pageable, Long id, String name, String manufacturer);

    Vermifuge save(Vermifuge newVermifuge);

    void deleteById(Long id);
}
