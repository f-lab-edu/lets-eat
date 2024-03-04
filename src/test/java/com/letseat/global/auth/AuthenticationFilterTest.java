package com.letseat.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.member.domain.Member;
import com.letseat.member.repository.MemberRepository;
import com.letseat.global.login.dto.LoginRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AuthenticationFilterTest {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() {
        String encryptPassword = passwordEncoder.encode("inho5389!");
        Member member =
                createMember("inho5389", encryptPassword, "정인호", "test@google.com", "01012345678");
        memberRepository.save(member);
    }

    @AfterEach
    public void tearDown(){
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 요청시 username과 password가 맞으면 리턴해주는 토큰과 로그인id를 검증한다.")
    void successfulAuthentication_test() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("inho5389", "inho5389!");
        String requestBody = om.writeValueAsString(loginRequestDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
        String jwtToken = resultActions.andReturn().getResponse().getHeader("Authorization");

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberDto.loginId").value("inho5389"))
                .andExpect(jsonPath("$.message").value("로그인이 성공했습니다."));
        assertThat(jwtToken.startsWith("Bearer "));
    }
    @Test
    @DisplayName("로그인 요청시 username이 틀릴때 반환 메시지를 검증한다.")
    void unSuccessfulAuthenticationNonMatchUsername() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("inho53899", "inho5389!");
        String requestBody = om.writeValueAsString(loginRequestDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value(false))
                .andExpect(jsonPath("$.message").value("아이디 혹은 비밀번호를 잘못 입력하셨거나 등록되지 않은 아이디 입니다."));
    }

    @Test
    @DisplayName("로그인 요청시 password가 틀릴때 반환 메시지를 검증한다.")
    void unSuccessfulAuthenticationNonMatchPassword() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("inho5389", "inho5389");
        String requestBody = om.writeValueAsString(loginRequestDto);

        //when
        ResultActions resultActions = mockMvc.perform(post("/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value(false))
                .andExpect(jsonPath("$.message").value("아이디 혹은 비밀번호를 잘못 입력하셨거나 등록되지 않은 아이디 입니다."));
    }

    private static Member createMember(String loginId, String password, String name, String email, String phone) {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phone(phone)
                .role(Role.ROLE_MEMBER)
                .build();
    }
}