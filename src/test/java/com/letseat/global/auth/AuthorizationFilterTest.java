package com.letseat.global.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthorizationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Authorization 헤더가 없고 권한이 맞을 때")
    void authorizationHeaderIsEmptyRoleIsCorrect() throws Exception {
        String encryptPassword = passwordEncoder.encode("inho5389!");

        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678",Role.ROLE_MEMBER);
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        mockMvc.perform(post("/api/members/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("로그인 하여 주세요."));
    }

    @Test
    @DisplayName("Authorization 헤더가 없고 권한이 맞지 않을 때")
    void authorizationHeaderIsEmptyRoleIsIncorrect() throws Exception {
        String encryptPassword = passwordEncoder.encode("inho5389!");

        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678",Role.ROLE_MEMBER);
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        mockMvc.perform(post("/api/owners/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("로그인 하여 주세요."));
    }

    @Test
    @DisplayName("Authorization 헤더가 있고 권한이 맞고 토큰이 유효하지만 컨트롤러가 없을 경우")
    void authorizationHeaderIsCorrectRoleIsCorrect() throws Exception {
        String encryptPassword = passwordEncoder.encode("inho5389!");

        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678",Role.ROLE_MEMBER);
        memberRepository.save(member);
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        String jwtToken = createIdUsernameToken(principalDetails, "testToken");

        mockMvc.perform(post("/api/members/test")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("Authorization 헤더가 있고 권한이 맞지 않고 토큰이 유효하지만 컨트롤러가 없을 경우")
    void authorizationHeaderIsCorrectRoleIsIncorrect() throws Exception {
        String encryptPassword = passwordEncoder.encode("inho5389!");

        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678",Role.ROLE_MEMBER);
        memberRepository.save(member);
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        String jwtToken = createIdUsernameToken(principalDetails, "testToken");

        mockMvc.perform(post("/api/owners/test")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."));
    }
    @Test
    @DisplayName("Authorization 헤더가 있고 권한이 맞고 토큰이 유효하지않을 경우")
    void authorizationHeaderIsCorrectRoleIsCorrectTokenExpired() throws Exception {
        String encryptPassword = passwordEncoder.encode("inho5389!");

        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678",Role.ROLE_MEMBER);
        memberRepository.save(member);
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        String jwtToken = createIdUsernameTokenExpired(principalDetails, "testToken");

        mockMvc.perform(post("/api/members/test")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("jwt 토큰이 유효하지 않습니다. 다시 로그인해주세요."));
    }
    @Test
    @DisplayName("Authorization 헤더가 있고 권한이 맞지않고 토큰이 유효하지않을 경우")
    void authorizationHeaderIsCorrectRoleIsInCorrectTokenExpired() throws Exception {
        String encryptPassword = passwordEncoder.encode("inho5389!");

        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678",Role.ROLE_MEMBER);
        memberRepository.save(member);
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        String jwtToken = createIdUsernameTokenExpired(principalDetails, "testToken");

        mockMvc.perform(post("/api/owners/test")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("jwt 토큰이 유효하지 않습니다. 다시 로그인해주세요."));
    }

    private String createIdUsernameToken(PrincipalDetails principalDetails, String tokenName) {
        return JWT.create()
                .withSubject(tokenName)
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getLoginId())
                .sign(Algorithm.HMAC512("secretKey"));
    }
    private String createIdUsernameTokenExpired(PrincipalDetails principalDetails, String tokenName) {
        return JWT.create()
                .withSubject(tokenName)
                .withExpiresAt(new Date(System.currentTimeMillis() + -1))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getLoginId())
                .sign(Algorithm.HMAC512("secretKey"));
    }

    private static Member createMember(String loginId, String password, String name, String email, String phone, Role role) {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phone(phone)
                .role(role)
                .build();
    }
}
