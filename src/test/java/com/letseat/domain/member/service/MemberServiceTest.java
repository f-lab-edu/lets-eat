package com.letseat.domain.member.service;

import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.dto.response.MemberResponse;
import com.letseat.domain.member.exception.join.AlreadyExistUserException;
import com.letseat.domain.member.repository.MemberRepository;
import com.letseat.global.auth.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원이 회원가입을 하고 DB에 잘 저장되어있는지 확인한다.")
    void memberJoin(){
        //given
        MemberSignUpRequest request =
                createMemberSignUpRequest("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678");

        //when
        MemberResponse memberResponse = memberService.join(request);
        Member member = memberRepository.findByLoginId(request.getLoginId()).get();

        //then
        assertThat(memberResponse.getId()).isEqualTo(member.getId());
        assertThat(member)
                .extracting("loginId","name","email","phone")
                .containsExactlyInAnyOrder("inho5389", "정인호", "test@google.com", "01012345678");
    }
    @Test
    @DisplayName("회원가입시 중복된 아이디로 회원가입시 예외가 발생한다.")
    void duplicateMemberJoin(){
        //given
        Member member1 = createMember("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678");
        memberRepository.save(member1);
        MemberSignUpRequest request =
                createMemberSignUpRequest("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678");

        //when
        //then
        assertThatThrownBy(()->memberService.join(request))
                .isInstanceOf(AlreadyExistUserException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @Test
    @DisplayName("회원가입시 비밀번호가 암호화 되어 있는지 확인한다.")
    void encryptionPasswordWhenJoin(){
        //given
        MemberSignUpRequest request =
                createMemberSignUpRequest("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678");
        memberService.join(request);
        Member member = memberRepository.findByLoginId(request.getLoginId()).get();

        //when
        boolean result = passwordEncoder.matches(request.getPassword(), member.getPassword());
        //then
        assertThat(result).isTrue();
    }

    private static MemberSignUpRequest createMemberSignUpRequest(String loginId, String password, String name, String email, String phone) {
        return MemberSignUpRequest.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phone(phone)
                .build();
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