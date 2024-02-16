package com.letseat.domain.member.service;

import com.letseat.domain.member.dto.request.MemberSignUpRequest;
import com.letseat.domain.member.repository.MemberRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberValidationService {

    private static final Pattern passwordRegex = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
    private static final Pattern emailRegex = Pattern.compile("\\w+@\\w+\\.\\w+(\\.\\w+)?");
    private static final int MIN_LOGIN_ID_LENGTH = 4;
    private static final int MAX_LOGIN_ID_LENGTH = 11;

    private final MemberRepository memberRepository;


    public void isSignUpValid(MemberSignUpRequest request){
        memberRepository.findByLoginId(request.getLoginId()).ifPresent(
                member -> {
                    throw new ValidationException("이미 존재하는 아이디입니다.");
                }
        );
        isLoginIdValid(request.getLoginId());
        isPasswordValid(request.getPassword());
        isEmailValid(request.getEmail());

    }
    public void isLoginIdValid(String loginId) {
        if (StringUtils.isEmpty(loginId)) {
            throw new ValidationException("로그인 아이디를 입력해주세요.");
        }

        if (loginId.length() <= MIN_LOGIN_ID_LENGTH || loginId.length() >= MAX_LOGIN_ID_LENGTH) {
            throw new ValidationException("아이디는 5자이상 10글자 이하입니다.");
        }
    }

    public void isPasswordValid(String password) {

        Matcher matcher = passwordRegex.matcher(password);

        if (!matcher.matches()) {
            throw new ValidationException("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.");
        }
    }

    public void isEmailValid(String email) {
        Matcher matcher = emailRegex.matcher(email);

        if (!matcher.matches()) {
            throw new ValidationException("이메일 형식이 올바르지 않습니다.");
        }
    }
}
