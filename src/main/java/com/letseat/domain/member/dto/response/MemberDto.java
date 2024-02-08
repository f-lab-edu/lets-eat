package com.letseat.domain.member.dto.response;

import com.letseat.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;
    private String loginId;
    private String name;
    private String nickname;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    @Builder
    public MemberDto(Long id, String loginId, String name, String nickname, String message,LocalDateTime createdDateTime,
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
