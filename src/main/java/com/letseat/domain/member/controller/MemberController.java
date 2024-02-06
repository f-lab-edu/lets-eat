package com.letseat.domain.member.controller;

import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.dto.response.MemberResponse;
import com.letseat.domain.member.service.MemberService;
import com.letseat.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/new")
    public ApiResponse<MemberResponse> join(@RequestBody @Valid MemberSignUpRequest request) {
        MemberResponse memberResponse = memberService.join(request);

        return ApiResponse.of(HttpStatus.OK, memberResponse.getMessage(), memberResponse);
    }
}
