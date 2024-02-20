package com.letseat.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.dto.response.MemberSignUpDto;
import com.letseat.domain.member.repository.MemberRepository;
import com.letseat.global.auth.AuthenticationFilter;
import com.letseat.global.auth.AuthorizationFilter;
import com.letseat.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper om;
    private final MemberRepository memberRepository;

    // SecurityFilterChain 을 구현한 클래스는 보안 필터의 순서와 동작을 정의
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        // 예외 핸들링 부분
        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    // 인증되지 않은 사용자 접근 핸들러
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    String data = om.writeValueAsString(ApiResponse.of(HttpStatus.UNAUTHORIZED, "로그인 하여 주세요.", false));
                                    response.getWriter().write(data);
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    // 권한이 허용되지 않은 사용자 접근 핸들러
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    String data = om.writeValueAsString(ApiResponse.of(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다.", false));
                                    response.getWriter().write(data);
                                }));

        // 보안 설정 부분
        http
                .csrf(cs -> cs.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(fl -> fl.disable())
                .httpBasic(hb -> hb.disable());

        // 권한 설정 부분
        http
                .addFilter(new AuthenticationFilter(authenticationManager, om))
                .addFilter(new AuthorizationFilter(authenticationManager, memberRepository, om))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/members/**").hasAnyRole("MEMBER", "OWNER", "ADMIN")
                                .requestMatchers("/api/owners/**").hasAnyRole("OWNER", "ADMIN")
                                .requestMatchers("/api/admins/**").hasAnyRole("ADMIN")
                                .requestMatchers("/login").permitAll()
                                .anyRequest().permitAll());
        return http.build();
    }
}
