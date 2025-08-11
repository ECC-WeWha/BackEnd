package com.example.wewha.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = buildErrorResponse(errorCode, request.getRequestURI());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .status(ErrorCode.ERR_BAD_REQUEST.getStatus().value())
                .message(ex.getBindingResult().getFieldError().getDefaultMessage())
                .error(ErrorCode.ERR_BAD_REQUEST.getStatus().getReasonPhrase())
                .code(ErrorCode.ERR_BAD_REQUEST.getCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, ErrorCode.ERR_BAD_REQUEST.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(ErrorCode.ERR_INTERNAL_SERVER, request.getRequestURI());
        return new ResponseEntity<>(response, ErrorCode.ERR_INTERNAL_SERVER.getStatus());
    }

    private ErrorResponse buildErrorResponse(ErrorCode code, String path) {
        return ErrorResponse.builder()
                .status(code.getStatus().value())
                .message(code.getMessage())
                .error(code.getStatus().getReasonPhrase())
                .code(code.getCode())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
