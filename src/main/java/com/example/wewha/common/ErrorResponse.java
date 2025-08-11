package com.example.wewha.common;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private String error;
    private String code;
    private String path;
    private LocalDateTime timestamp;
}
