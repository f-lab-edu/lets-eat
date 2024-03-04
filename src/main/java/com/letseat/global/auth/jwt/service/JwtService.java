package com.letseat.global.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.member.domain.Member;
import com.letseat.global.auth.PrincipalDetails;
import com.letseat.global.auth.Role;
import com.letseat.global.auth.jwt.util.JwtUtil;
import com.letseat.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Date;

import static com.letseat.global.auth.jwt.util.JwtUtil.EXPIRATION_TIME;
import static com.letseat.global.auth.jwt.util.JwtUtil.SECRET_KEY;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
public class JwtService {

    public static String createIdUsernameToken(PrincipalDetails principalDetails, String tokenName){
        String jwtToken = JWT.create()
                .withSubject(tokenName)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getLoginId())
                .withClaim("role", principalDetails.getAuthorities().toString())
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return JwtUtil.TOKEN_PREFIX + jwtToken;
    }

    public static PrincipalDetails verify(String jwt, HttpServletResponse response, ObjectMapper objectMapper) throws IOException {

        DecodedJWT decodedJWT = null;
        try {
             decodedJWT = JWT.require(Algorithm.HMAC512(JwtUtil.SECRET_KEY))
                    .build()
                    .verify(jwt);
        }catch (JWTDecodeException | TokenExpiredException | NullPointerException e){
            commonHttpServletResponse(SC_BAD_REQUEST,response);
            String data = objectMapper.writeValueAsString(ApiResponse.of(HttpStatus.BAD_REQUEST, "jwt 토큰이 유효하지 않습니다. 다시 로그인해주세요.", null));
            response.getWriter().write(data);
            return null;
        }

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString().replace("[", "").replace("]", "");;
        Member member = Member.builder()
                .id(id)
                .role(Role.valueOf(role))
                .build();
        return new PrincipalDetails(member);
    }

    private static void commonHttpServletResponse(int code,HttpServletResponse response) {
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
