package com.letseat.domain.member.service;

import com.letseat.domain.member.domain.Member;
import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.dto.response.MemberResponse;
import com.letseat.domain.member.exception.join.AlreadyExistUserException;
import com.letseat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberValidationService memberValidationService;

    @Transactional
    public MemberResponse join(MemberSignUpRequest requestForm) {

        memberJoinValidation(requestForm);

        Member member = requestForm.signUpToEntity();
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

        return MemberResponse.of(member, "회원가입이 완료되었습니다.");
    }

    private void memberJoinValidation(MemberSignUpRequest requestForm) {
        memberValidationService.isLoginIdValid(requestForm.getLoginId());
        memberValidationService.isPasswordValid(requestForm.getPassword());
        memberValidationService.isEmailValid(requestForm.getEmail());

        memberRepository.findByLoginId(requestForm.getLoginId()).ifPresent(
                member -> {
                    throw new AlreadyExistUserException();
                }
        );
    }
}
