package com.letseat.domain.member.service;

import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceUnitTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberValidationService memberValidationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원이 회원가입을 하고 저장된 loginID를 반환하는지 확인한다.")
    void memberSignUp(){
        //given
        MemberSignUpRequest request = createMemberSignUpRequest("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678");
        Member savedMember = Member.builder()
                .loginId("inho5389")
                .build();

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn(request.getPassword());
        when(memberRepository.save(any())).thenReturn(savedMember);
        doNothing().when(memberValidationService).isSignUpValid(request);
        //when
        Member member = memberService.signUp(request);
        //then
        assertThat(member.getLoginId()).isEqualTo(savedMember.getLoginId());
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
}
