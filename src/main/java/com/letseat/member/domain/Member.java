package com.letseat.member.domain;

import com.letseat.global.common.BaseEntity;
import com.letseat.global.auth.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
}
