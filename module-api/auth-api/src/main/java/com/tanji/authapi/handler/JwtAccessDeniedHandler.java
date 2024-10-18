package com.tanji.authapi.handler;

import com.tanji.authapi.exception.JwtErrorCode;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.commonmodule.utils.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 인가(Authorization) 과정에서 발생하는 exception을 처리
 * 인증된 사용자이지만 해당 리소스에 대한 접근 권한이 없는 경우
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.warn("JwtAccessDeniedHandler: AccessDeniedException(AuthorizationException)", accessDeniedException);
        ResponseEntity<ApiResponse<Object>> fail = ApiResponse.fail(JwtErrorCode.FORBIDDEN_ACCESS.getHttpStatus(), JwtErrorCode.FORBIDDEN_ACCESS.getMessage());
        HttpResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN, fail);
    }
}