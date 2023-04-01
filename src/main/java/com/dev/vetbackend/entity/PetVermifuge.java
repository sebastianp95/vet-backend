package com.dev.vetbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@IdClass(PetVermifugeId.class)
@Table(name = "pet_vermifuge")
public class PetVermifuge {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Id
    @Column(name = "vermifuge_id")
    private String vermifugeId;

    @Column(name = "vermifuge_name")
    private String vermifugeName;
    @Column(name = "date")
    private LocalDate date;

}
