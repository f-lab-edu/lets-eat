package com.letseat.domain.member.service;

import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.exception.JoinCustomException;
import com.letseat.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberValidationServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberValidationService memberValidationService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 가입시 로그인 ID는 5글자 이상 10글자 이하여야 한다.")
    void isLoginIdValid() {
        //given
        String loginId = "inho5";
        //when
        //then
        memberValidationService.isLoginIdValid(loginId);
    }

    @Test
    @DisplayName("회원 가입시 로그인 ID가 4글자 이하면 예외를 일으킨다.")
    void isLoginIdValidLengthLessThan() {
        //given
        String loginId = "inho";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isLoginIdValid(loginId))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("아이디는 5자이상 10글자 이하입니다.");

    }

    @Test
    @DisplayName("회원 가입시 로그인 ID는 11글자 이상이면 예외를 일으킨다.")
    void isLoginIdValidLengthGreaterThan() {
        //given
        String loginId = "inho1234567";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isLoginIdValid(loginId))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("아이디는 5자이상 10글자 이하입니다.");
    }

    @Test
    @DisplayName("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.")
    void isPasswordValid() {
        //given
        String paasword = "inho5389!@";
        //when
        //then
        memberValidationService.isPasswordValid(paasword);
    }

    @Test
    @DisplayName("비밀번호의 영문만 들어갈 경우 예외를 일으킨다.")
    void isPasswordValidEnglishOnly() {
        //given
        String paasword = "inhohoho";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isPasswordValid(paasword))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.");
    }

    @Test
    @DisplayName("비밀번호의 숫자만 들어갈 경우 예외를 일으킨다.")
    void isPasswordValidNumbersOnly() {
        //given
        String paasword = "12345678";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isPasswordValid(paasword))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.");
    }

    @Test
    @DisplayName("비밀번호가 7글자 이하면 예외를 일으킨다.")
    void isPasswordValidLengthLessThan() {
        //given
        String paasword = "inho123";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isPasswordValid(paasword))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.");
    }

    @Test
    @DisplayName("비밀번호가 21글자 이상면 예외를 일으킨다.")
    void isPasswordValidLengthGreaterThan() {
        //given
        String paasword = "012345678901234567890";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isPasswordValid(paasword))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.");
    }

    @Test
    @DisplayName("이메일에는 @가 포함되어야 한다.")
    void isEmailValidWithOutWhelk() {
        //given
        String email = "qheogusnaver.com";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isEmailValid(email))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("이메일에는 .가 포함되어야 한다.")
    void isEmailValidWithOutPoint() {
        //given
        String email = "qheogus@navercom";
        //when
        //then
        assertThatThrownBy(() -> memberValidationService.isEmailValid(email))
                .isInstanceOf(JoinCustomException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("이메일에는 @나 .으로 로컬파트와 도메인 파트를 구분하여 양식을 지켜야한다.")
    void isEmailValid() {
        //given
        String email = "test@naver.com";
        //when
        //then
        memberValidationService.isEmailValid(email);
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