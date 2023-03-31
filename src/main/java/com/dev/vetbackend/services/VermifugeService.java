package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.Vermifuge;

import java.util.List;

public interface VermifugeService {


    List<Vermifuge> findAllByUser();

    Vermifuge save(Vermifuge newVermifuge);

    void deleteById(Long id);
}
