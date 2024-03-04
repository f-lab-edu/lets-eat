package com.letseat.member.dto.request;

import com.letseat.member.domain.Member;
import com.letseat.global.auth.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "핸드폰 번호를 입력해주세요.")
    private String phone;

    @Builder
    public MemberSignUpRequest(String loginId, String password, String email, String nickname, String name, String phone) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
    }

    public Member signUpToEntity(PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .nickname(nickname)
                .name(name)
                .phone(phone)
                .role(Role.ROLE_MEMBER)
                .build();
        member.encodePassword(passwordEncoder);
        return member;
    }
}
