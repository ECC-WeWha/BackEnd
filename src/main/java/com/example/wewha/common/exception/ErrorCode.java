package com.example.wewha.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ERR_BAD_REQUEST(HttpStatus.BAD_REQUEST, "ERR_BAD_REQUEST", "요청 형식이 올바르지 않습니다."),
    ERR_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ERR_UNAUTHORIZED", "인증이 필요합니다."),
    ERR_FORBIDDEN(HttpStatus.FORBIDDEN, "ERR_FORBIDDEN", "해당 리소스에 접근할 권한이 없습니다."),
    ERR_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR_NOT_FOUND", "요청하신 리소스를 찾을 수 없습니다."),
    ERR_CONFLICT(HttpStatus.CONFLICT, "ERR_CONFLICT", "이미 처리된 요청입니다."),
    ERR_UNPROCESSABLE(HttpStatus.UNPROCESSABLE_ENTITY, "ERR_UNPROCESSABLE", "입력값이 유효하지 않습니다."),
    ERR_INTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "ERR_INTERNAL_SERVER", "서버에서 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
