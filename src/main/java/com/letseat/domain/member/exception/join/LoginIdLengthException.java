package com.letseat.domain.member.exception.join;

import com.letseat.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LoginIdLengthException extends CustomException {

    private final String message = "아이디는 5자이상 10글자 이하입니다.";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
