package com.letseat.global.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.response.MemberSignUpDto;
import com.letseat.global.auth.util.JwtUtil;
import com.letseat.global.common.ApiResponse;
import com.letseat.global.login.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static com.letseat.global.auth.util.JwtUtil.*;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper om;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도중");
        LoginRequestDto requestDto;

        try {
            requestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("===============  successfulAuthentication =====================");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Member member = ((PrincipalDetails) authResult.getPrincipal()).getMember();
        String jwtToken = createIdUsernameToken(principalDetails, "letseat_token");

        response.addHeader(HEADER, TOKEN_PREFIX + jwtToken);

        String data = om.writeValueAsString(ApiResponse.of(HttpStatus.OK, "로그인이 성공했습니다.", MemberSignUpDto.of(member, TOKEN_PREFIX + jwtToken)));
        response.getWriter().write(data);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("===============  unsuccessfulAuthentication =====================");
        response.setStatus(SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (failed instanceof BadCredentialsException) {
            String data = om.writeValueAsString(ApiResponse.of(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 잘못 입력하셨거나 등록되지 않은 아이디 입니다.", false));
            response.getWriter().write(data);
        }
    }

    private String createIdUsernameToken(PrincipalDetails principalDetails, String tokenName) {
        return JWT.create()
                .withSubject(tokenName)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getLoginId())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }
}
