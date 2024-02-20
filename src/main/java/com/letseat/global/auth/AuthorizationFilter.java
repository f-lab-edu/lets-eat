package com.letseat.global.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.repository.MemberRepository;
import com.letseat.global.common.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

import static com.letseat.global.auth.util.JwtUtil.*;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

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

        boolean isTokenValid = true;

        String jwtToken = request.getHeader(HEADER);

        if (jwtToken == null || !jwtToken.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        String jwt = request.getHeader(HEADER).replace(TOKEN_PREFIX, "");

        String loginId = null;

        try {
            loginId = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(jwt)
                    .getClaim("username")
                    .asString();
        }catch (JWTDecodeException  | TokenExpiredException e){
            isTokenValid = false;
        }

        // 토큰이 유효하지 않은  경우
        if (!isTokenValid) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String data = objectMapper.writeValueAsString(ApiResponse.of(HttpStatus.BAD_REQUEST, "jwt 토큰이 유효하지 않습니다. 다시 로그인해주세요.", false));
            response.getWriter().write(data);
            return;
        }

        if (loginId != null){
            // 위에서 jwt를 decode하고 username을 꺼내왔지만 한번 더 리포지토리에서 검증이 필요한지??..
            Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
            if (optionalMember.isEmpty()){
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String data = objectMapper.writeValueAsString(ApiResponse.of(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", false));
                response.getWriter().write(data);
            }
            Member member = optionalMember.get();

            PrincipalDetails principalDetails = new PrincipalDetails(member);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }
}
