package com.dev.vetbackend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PetVaccinationDTO {
    private Long id;
    private Long petId;
    private Long vaccinationId;
    private String vaccinationName;
    private String vaccinationType;
    private LocalDate date;
}
