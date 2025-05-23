package com.tanji.authapi.handler;

import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.commonmodule.utils.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.tanji.authapi.exception.AuthErrorCode.UNAUTHORIZED_MEMBER;

/**
 * 인증(Authentication) 과정에서 발생하는 exception을 처리
 * 로그인이 필요한 API에 인증 없이 접근할 때 이 핸들러가 호출
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.error("[*] AuthenticationException: ", authException);
        ResponseEntity<ApiResponse<Object>> fail = ApiResponse.fail(UNAUTHORIZED_MEMBER.getHttpStatus(), UNAUTHORIZED_MEMBER.getMessage());
        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, fail);
    }
}