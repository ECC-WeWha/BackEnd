package com.example.wewha.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String target) {
        super(target + " not found");
    }
}