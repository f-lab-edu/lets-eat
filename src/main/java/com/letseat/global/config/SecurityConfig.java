package com.letseat.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    CorsConfig corsConfig;

    @Autowired
    UserDetailsService userDetailsService;

    // SecurityFilterChain 을 구현한 클래스는 보안 필터의 순서와 동작을 정의
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 보안 설정 부분
        http
                .csrf(cs -> cs.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(fl -> fl.disable())
                .httpBasic(hb -> hb.disable());

        // 권한 설정 부분
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/members/**").hasAnyRole("MEMBER", "OWNER", "ADMIN")
                                .requestMatchers("/api/owners/**").hasAnyRole("OWNER", "ADMIN")
                                .requestMatchers("/api/admins/**").hasAnyRole("ADMIN")
                                .anyRequest().permitAll());
        return http.build();
    }
}
