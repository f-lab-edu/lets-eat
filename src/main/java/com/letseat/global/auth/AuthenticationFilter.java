package com.letseat.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.response.MemberSignUpDto;
import com.letseat.global.auth.jwt.service.JwtService;
import com.letseat.global.common.ApiResponse;
import com.letseat.global.login.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;;
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

import static com.letseat.global.auth.jwt.util.JwtUtil.*;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

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
        commonHttpServletResponse(SC_OK,response);

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Member member = ((PrincipalDetails) authResult.getPrincipal()).getMember();
        String jwtToken = JwtService.createIdUsernameToken(principalDetails, "letseat_token");

        response.addHeader(HEADER, jwtToken);

        String data = om.writeValueAsString(ApiResponse.of(HttpStatus.OK, "로그인이 성공했습니다.", MemberSignUpDto.of(member, jwtToken)));
        response.getWriter().write(data);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("===============  unsuccessfulAuthentication =====================");
        commonHttpServletResponse(SC_BAD_REQUEST,response);
        if (failed instanceof BadCredentialsException) {
            String data = om.writeValueAsString(ApiResponse.of(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 잘못 입력하셨거나 등록되지 않은 아이디 입니다.", null));
            response.getWriter().write(data);
        }
    }

    private static void commonHttpServletResponse(int code,HttpServletResponse response) {
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
