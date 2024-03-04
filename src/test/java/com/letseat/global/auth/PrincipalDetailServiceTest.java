package com.letseat.global.auth;

import com.letseat.member.domain.Member;
import com.letseat.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrincipalDetailServiceTest {

    @InjectMocks
    private PrincipalDetailService principalDetailService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원이 저장 되어 있을때 loadUserByUsername 실행시 저장된 회원의 loginId로 Member객체가 잘 찾아오는지 확인하는 테스트")
    void loadUserByUsernameMemberExist() {
        //given
        String loginId = "inho5389";
        Member member = createMember(loginId, "inho5389!", "정인호", "test@google.com", "01012345678");
        when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));
        //when
        UserDetails userDetails = principalDetailService.loadUserByUsername(loginId);
        //then
        assertThat(userDetails).isNotNull();
        assertThat(loginId).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("회원이 저장 되어 있지 않을때 loadUserByUsername 실행시 예외를 확인하는 테스트")
    void loadUserByUsernameMemberNotExist() {
        //given
        String loginId = "inho5389";
        when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThatThrownBy(() -> principalDetailService.loadUserByUsername(loginId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("");
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