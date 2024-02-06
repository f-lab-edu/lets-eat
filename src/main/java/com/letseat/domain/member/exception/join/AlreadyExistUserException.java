package com.letseat.domain.member.exception.join;

import com.letseat.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyExistUserException extends CustomException {

    private static final String message = "이미 존재하는 아이디입니다.";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
