package com.example.wewha.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 요청한 리소스를 찾지 못했을 때 던지는 예외.
 * GlobalExceptionHandler가 있다면 @ResponseStatus 없어도 처리 가능하지만,
 * 단독 사용 시 404를 보장하기 위해 붙여둡니다.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("리소스를 찾을 수 없습니다.");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s 를(을) 찾을 수 없습니다. (%s = %s)",
                resourceName, fieldName, String.valueOf(fieldValue)));
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
