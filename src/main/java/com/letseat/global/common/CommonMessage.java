package com.letseat.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonMessage {

    JOIN_COMPLETE("회원가입이 완료되었습니다.");

    private final String message;
}
