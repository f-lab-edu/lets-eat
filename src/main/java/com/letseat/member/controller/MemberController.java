package com.letseat.member.controller;

import com.letseat.member.domain.Member;
import com.letseat.member.dto.request.MemberSignUpRequest;
import com.letseat.member.dto.response.MemberDto;
import com.letseat.member.service.MemberService;
import com.letseat.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.letseat.global.common.CommonMessage.JOIN_COMPLETE;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ApiResponse<MemberDto> signUp(@RequestBody @Valid MemberSignUpRequest request) {
        Member savedMember = memberService.signUp(request);
        MemberDto memberDto = MemberDto.of(savedMember);
        return ApiResponse.of(HttpStatus.OK, JOIN_COMPLETE.getMessage(), memberDto);
    }

}
