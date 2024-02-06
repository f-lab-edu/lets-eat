package com.letseat.domain.member.exception.join;

import com.letseat.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LoginIdEmptyException extends CustomException {

    private final String message = "로그인 아이디를 입력해주세요";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
