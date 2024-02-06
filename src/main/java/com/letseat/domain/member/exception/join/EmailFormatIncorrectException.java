package com.letseat.domain.member.exception.join;

import com.letseat.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class EmailFormatIncorrectException extends CustomException {

    private final String message = "이메일 형식이 올바르지 않습니다.";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
