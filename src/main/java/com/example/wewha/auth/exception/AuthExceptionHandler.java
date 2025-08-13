package com.example.wewha.auth.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
// auth 패키지 하위 컨트롤러들에만 적용 (필요 시 경로 조정)
@RestControllerAdvice(basePackages = "com.example.wewha.auth")
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("error", ex.getErrorCode().name());
        response.put("message", ex.getMessage());


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


}
