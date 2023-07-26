package com.dev.vetbackend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String imageSrc;
    private boolean printImage;
    private String secondEmail;
    private Long phone;

}
