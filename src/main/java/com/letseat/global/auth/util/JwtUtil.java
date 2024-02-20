package com.letseat.global.auth.util;

public interface JwtUtil {
    String SECRET_KEY = "secretKey";
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "Authorization";
}
