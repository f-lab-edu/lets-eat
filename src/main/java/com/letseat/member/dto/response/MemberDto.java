package com.letseat.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.letseat.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;
    private String loginId;
    private String name;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime modifiedDateTime;

    @Builder
    public MemberDto(Long id, String loginId, String name, String nickname, LocalDateTime createdDateTime,
                     LocalDateTime modifiedDateTime) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .nickname(member.getNickname())
                .createdDateTime(member.getCreatedDateTime())
                .modifiedDateTime(member.getModifiedDateTime())
                .build();
    }
}
