package com.letseat.domain.member.service;

import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.exception.JoinCustomException;
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


    public Member join(MemberSignUpRequest requestForm) {

        memberJoinValidation(requestForm);

        Member member = requestForm.signUpToEntity();
        member.encodePassword(passwordEncoder);
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    private void memberJoinValidation(MemberSignUpRequest requestForm) {
        memberValidationService.isLoginIdValid(requestForm.getLoginId());
        memberValidationService.isPasswordValid(requestForm.getPassword());
        memberValidationService.isEmailValid(requestForm.getEmail());

        memberRepository.findByLoginId(requestForm.getLoginId()).ifPresent(
                member -> {
                    throw new JoinCustomException("이미 존재하는 아이디입니다.");
                }
        );
    }
}
