package com.letseat.domain.member.service;

import com.letseat.domain.member.exception.join.EmailFormatIncorrectException;
import com.letseat.domain.member.exception.join.LoginIdEmptyException;
import com.letseat.domain.member.exception.join.LoginIdLengthException;
import com.letseat.domain.member.exception.join.PasswordNotMatchException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberValidationService {

    public boolean isLoginIdValid(String loginId) {
        if (loginId == null || loginId.isEmpty()) {
            throw new LoginIdEmptyException();
        }

        if (loginId.length() <= 4 || loginId.length() >= 11) {
            throw new LoginIdLengthException();
        }
        return true;
    }

    public boolean isPasswordValid(String password) {

        String text = password;
        Pattern passPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher matcher = passPattern.matcher(text);

        if (!matcher.matches()) {
            throw new PasswordNotMatchException();
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        String text = email;
        Pattern passPattern = Pattern.compile("\\w+@\\w+\\.\\w+(\\.\\w+)?");
        Matcher matcher = passPattern.matcher(text);

        if (!matcher.matches()) {
            throw new EmailFormatIncorrectException();
        }

        return true;
    }
}
