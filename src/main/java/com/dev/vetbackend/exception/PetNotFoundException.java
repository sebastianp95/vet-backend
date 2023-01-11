package com.dev.vetbackend.exception;

public class PetNotFoundException extends RuntimeException {
    //Constructor with message
    public PetNotFoundException(String message) {
        super(message);
    }

    //Constructor without message
    public PetNotFoundException() {
        super();
    }
}