package com.dev.vetbackend.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}