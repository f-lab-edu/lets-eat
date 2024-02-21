package com.letseat.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.repository.MemberRepository;
import com.letseat.global.auth.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static com.letseat.global.auth.jwt.util.JwtUtil.HEADER;
import static com.letseat.global.auth.jwt.util.JwtUtil.TOKEN_PREFIX;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {


    private MemberRepository memberRepository;
    private ObjectMapper objectMapper;

    public AuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthorizationFilter.doFilterInternal");

        if (isHeaderVerify(request, response)){
            String jwt = request.getHeader(HEADER).replace(TOKEN_PREFIX, "");

            PrincipalDetails loginMember = JwtService.verify(jwt,response,objectMapper);
            if (loginMember == null){
                chain.doFilter(request,response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(loginMember, null, loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request,response);

    }

    private static boolean isHeaderVerify(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String jwtToken = request.getHeader(HEADER);

        if (jwtToken == null || !jwtToken.startsWith(TOKEN_PREFIX)) {
            return false;
        }else {
            return true;
        }
    }


}
