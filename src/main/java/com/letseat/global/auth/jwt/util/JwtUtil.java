package com.letseat.global.auth.jwt.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public interface JwtUtil {

    String SECRET_KEY = System.getenv("JWT_SECRET");
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "Authorization";
}
