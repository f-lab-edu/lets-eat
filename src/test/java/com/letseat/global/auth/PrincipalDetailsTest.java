package com.letseat.global.auth;

import com.letseat.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static com.letseat.global.auth.Role.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

class PrincipalDetailsTest {

    @Test
    @DisplayName("회원이 저장되어있다고 가정하에 그 회원의 권한을 가져올 때 잘 가져오는지 확인하는 테스트.")
    void getAuthorities(){
        //given
        Role role = ROLE_MEMBER;
        Member member = createMember("inho5389", "inho5389!", "정인호", "test@google.com", "01012345678",role);
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        //when
        Collection<? extends GrantedAuthority> authorities = principalDetails.getAuthorities();
        //then
        assertThat(authorities).extracting(grantedAuthority -> grantedAuthority.getAuthority())
                .contains(String.valueOf(role));
    }

    private static Member createMember(String loginId, String password, String name, String email, String phone,Role role) {
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