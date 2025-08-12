package com.example.wewha.comments.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String target) {
        super(target + " not found");
    }
}