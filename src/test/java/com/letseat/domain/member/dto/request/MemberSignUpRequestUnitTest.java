package com.letseat.domain.member.dto.request;

import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.repository.MemberRepository;
import com.letseat.global.auth.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberSignUpRequestUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("비밀번호가 암호화 되어 있는지 확인한다.")
    public void encryptPasswordSuccess() {
        // given
        String encryptedPassword = "encryptedPassword";

        MemberSignUpRequest signUpRequest = createMemberSignUpRequest("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678");
        when(passwordEncoder.encode("inho5389!")).thenReturn(encryptedPassword);
        when(memberRepository.save(any(Member.class))).thenReturn(
                createMember("inho5389", encryptedPassword, "test@google.com", "정인호", "01012345678", Role.ROLE_MEMBER)
        );

        // when
        Member savedMember = signUpRequest.signUpToEntity(memberRepository, passwordEncoder);

        // then
        verify(passwordEncoder).encode("inho5389!");
        verify(memberRepository).save(any(Member.class));
        assertEquals(encryptedPassword, savedMember.getPassword());
    }

    private static Member createMember(String loginId, String password, String email, String name, String phone, Role role) {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .phone(phone)
                .role(role)
                .build();
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