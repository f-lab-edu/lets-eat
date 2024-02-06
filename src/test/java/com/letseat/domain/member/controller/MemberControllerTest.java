package com.letseat.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.dto.response.MemberResponse;
import com.letseat.domain.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @AfterEach
    void tearDown() {

    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입한다.")
    void createMember() throws Exception {
        //given
        MemberSignUpRequest request =
                createMemberSignUpRequest("inho5389", "1234", "이노", "정인호", "test@google.com", "01012345678");

        MemberResponse memberResponse = MemberResponse.builder()
                .id(1L)
                .loginId(request.getLoginId())
                .name(request.getName())
                .nickname(request.getNickname())
                .message("회원가입이 완료되었습니다.")
                .build();

        Mockito.when(memberService.join(any(MemberSignUpRequest.class))).thenReturn(memberResponse);
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입할때 로그인 ID는 필수이다..")
    void createMemberWithoutLoginId() throws Exception {
        //given
        MemberSignUpRequest request =
                MemberSignUpRequest.builder()
                        .password("1234")
                        .nickname("이노")
                        .name("정인호")
                        .email("test@google.com")
                        .phone("01012345678")
                        .build();
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("아이디를 입렵해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입할때 패스워드는 필수이다..")
    void createMemberWithoutPassword() throws Exception {
        //given
        MemberSignUpRequest request =
                MemberSignUpRequest.builder()
                        .loginId("inho5389")
                        .nickname("이노")
                        .name("정인호")
                        .email("test@google.com")
                        .phone("01012345678")
                        .build();
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("비밀번호를 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입할때 이메일은 필수이다..")
    void createMemberWithoutEmail() throws Exception {
        //given
        MemberSignUpRequest request =
                MemberSignUpRequest.builder()
                        .loginId("inho5389")
                        .password("123")
                        .nickname("이노")
                        .name("정인호")
                        .phone("01012345678")
                        .build();
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일을 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입할때 닉네임은 필수이다..")
    void createMemberWithoutNickname() throws Exception {
        //given
        MemberSignUpRequest request =
                MemberSignUpRequest.builder()
                        .loginId("inho5389")
                        .password("1234")
                        .name("정인호")
                        .email("test@google.com")
                        .phone("01012345678")
                        .build();
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("닉네임을 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입할때 이름은 필수이다..")
    void createMemberWithoutName() throws Exception {
        //given
        MemberSignUpRequest request =
                MemberSignUpRequest.builder()
                        .loginId("inho5389")
                        .password("1234")
                        .nickname("이노")
                        .email("test@google.com")
                        .phone("01012345678")
                        .build();
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이름을 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "회원", roles = "USER")
    @DisplayName("신규 회원을 회원가입할때 핸드폰번호는 필수이다..")
    void createMemberWithoutPhone() throws Exception {
        //given
        MemberSignUpRequest request =
                MemberSignUpRequest.builder()
                        .loginId("inho5389")
                        .password("1234")
                        .nickname("이노")
                        .name("정인호")
                        .email("test@google.com")
                        .build();
        //when
        //then
        mockMvc.perform(post("/member/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("핸드폰 번호를 입력해주세요."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private static MemberSignUpRequest createMemberSignUpRequest(String loginId, String password, String nickname, String name, String email, String phone) {
        return MemberSignUpRequest.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .build();
    }
}