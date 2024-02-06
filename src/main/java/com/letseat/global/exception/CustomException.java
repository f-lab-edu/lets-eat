package com.letseat.global.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    public abstract HttpStatus getStatus();

    public abstract String getMessage();

}
