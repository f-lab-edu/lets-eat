package com.letseat.global.exception;

import com.letseat.global.common.ApiResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> validationCustomHandler(ValidationException e) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(statusCode)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST,e.getMessage(), false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        List<String> collect = allErrors.stream().map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ApiResponse.of(HttpStatus.BAD_REQUEST,"바인딩 오류", collect);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> validationCustomHandler(UsernameNotFoundException e) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(statusCode)
                .body(ApiResponse.of(HttpStatus.BAD_REQUEST,e.getMessage(), false));
    }
}