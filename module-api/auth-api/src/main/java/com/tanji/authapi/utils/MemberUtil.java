package com.tanji.authapi.utils;

import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.authapi.exception.AuthErrorCode;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import static com.tanji.authapi.exception.AuthErrorCode.UNAUTHORIZED_MEMBER;

@RequiredArgsConstructor
public class MemberUtil {
    /**
     * 현재 사용자의 OAuth2 ID를 반환합니다.
     *
     * @param principal: 현재 사용자의 Principal 객체
     * @return 현재 사용자의 OAuth2 ID
     */
    public static Long getMemberId(Principal principal) {
        // Principal 객체가 null이면 사용자가 인가되지 않았으므로 예외를 발생시킨다.
        if (principal == null) {
            throw new AuthCustomException(UNAUTHORIZED_MEMBER);
        }
        // Principal 객체의 이름을 memberId ID로 사용하여 반환한다.
        return Long.valueOf(principal.getName());
    }
}
