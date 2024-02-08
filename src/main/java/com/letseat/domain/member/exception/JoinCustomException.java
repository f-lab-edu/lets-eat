package com.letseat.domain.member.exception;

import com.letseat.global.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JoinCustomException extends CustomException {

    private String message;

    public JoinCustomException(String message) {
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
