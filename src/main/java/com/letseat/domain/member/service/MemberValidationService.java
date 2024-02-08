package com.letseat.domain.member.service;

import com.letseat.domain.member.exception.JoinCustomException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberValidationService {

    private static final Pattern passwordRegex = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
    private static final Pattern emailRegex = Pattern.compile("\\w+@\\w+\\.\\w+(\\.\\w+)?");


    public void isLoginIdValid(String loginId) {
        if (loginId == null || loginId.isEmpty()) {
            throw new JoinCustomException("로그인 아이디를 입력해주세요.");
        }

        if (loginId.length() <= 4 || loginId.length() >= 11) {
            throw new JoinCustomException("아이디는 5자이상 10글자 이하입니다.");
        }
    }

    public void isPasswordValid(String password) {

        Matcher matcher = passwordRegex.matcher(password);

        if (!matcher.matches()) {
            throw new JoinCustomException("비밀번호는 8글자 이상 20글자 이하 그리고 숫자,영문,특수문자가 1개 이상 포함되어야합니다.");
        }
    }

    public void isEmailValid(String email) {
        Matcher matcher = emailRegex.matcher(email);

        if (!matcher.matches()) {
            throw new JoinCustomException("이메일 형식이 올바르지 않습니다.");
        }
    }
}
