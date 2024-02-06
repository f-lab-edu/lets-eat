package com.letseat.global.exception;

import com.letseat.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> customHandler(CustomException e) {
        int statusCode = e.getStatus().value();
        return ResponseEntity.status(statusCode)
                .body(ApiResponse.of(e.getStatus(), e.getMessage(), false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        ObjectError data = e.getBindingResult().getAllErrors().get(0);
        return ApiResponse.of(HttpStatus.BAD_REQUEST, data.getDefaultMessage(), null);
    }
}