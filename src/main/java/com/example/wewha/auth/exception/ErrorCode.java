package com.example.wewha.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMAIL_DUPLICATE("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACADEMIC_STATUS("유효하지 않은 학적 ID입니다.", HttpStatus.BAD_REQUEST),
    INVALID_REGION("유효하지 않은 지역 ID입니다.", HttpStatus.BAD_REQUEST),
    INVALID_LANGUAGE("유효하지 않은 모국어 ID입니다.", HttpStatus.BAD_REQUEST),
    INVALID_STUDY_LANGUAGE("유효하지 않은 학습 언어 ID입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
