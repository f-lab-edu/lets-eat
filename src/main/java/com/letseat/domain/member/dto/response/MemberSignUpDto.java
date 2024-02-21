package com.letseat.domain.member.dto.response;

import com.letseat.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignUpDto {

    private MemberDto memberDto;
    private String jwtToken;
    private boolean result;

    public static MemberSignUpDto of(Member member,String jwtToken) {
        return MemberSignUpDto.builder()
                .memberDto(MemberDto.of(member))
                .jwtToken(jwtToken)
                .result(true)
                .build();
    }

}
