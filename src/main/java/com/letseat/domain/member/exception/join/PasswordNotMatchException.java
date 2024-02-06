package com.letseat.domain.member.exception.join;

import com.letseat.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends CustomException {

    private final String message = "비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.";

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
