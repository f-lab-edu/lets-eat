package com.letseat.domain.member.service;

import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberValidationService memberValidationService;


    public Member signUp(MemberSignUpRequest requestForm) {

        memberValidationService.isSignUpValid(requestForm);
        Member savedMember = requestForm.signUpToEntity(memberRepository, passwordEncoder);

        return savedMember;
    }
}
