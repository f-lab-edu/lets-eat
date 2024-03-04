package com.letseat.member.service;

import com.letseat.member.domain.Member;
import com.letseat.member.dto.request.MemberSignUpRequest;
import com.letseat.member.repository.MemberRepository;
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
        Member member = requestForm.signUpToEntity(passwordEncoder);

        return memberRepository.save(member);
    }
}
