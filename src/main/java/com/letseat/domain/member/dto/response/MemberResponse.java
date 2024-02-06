package com.letseat.domain.member.dto.response;

import com.letseat.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Long id;
    private String loginId;
    private String name;
    private String nickname;
    private String message;

    @Builder
    public MemberResponse(Long id, String loginId, String name, String nickname, String message) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.message = message;
    }

    public static MemberResponse of(Member member, String message) {
        return MemberResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .nickname(member.getNickname())
                .message(message)
                .build();
    }
}
