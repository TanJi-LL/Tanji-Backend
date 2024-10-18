package com.tanji.authapi.jwt;

import com.tanji.authapi.exception.JwtErrorCode;
import com.tanji.commonmodule.exception.CommonErrorCode;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.commonmodule.utils.HttpResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰을 검증하고, 검증이 성공한 경우 인증 객체 Authentication를 생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final HttpResponseUtil httpResponseUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtToken = jwtUtil.parseToken(request);
        if (jwtToken != null) {
            try {
                jwtUtil.validateTokenV2(jwtToken);
                setAuthentication(jwtToken);
            } catch (SecurityException | MalformedJwtException e) {
                log.warn("잘못된 토큰: {}", e.getMessage());
                httpResponseUtil.setErrorResponse(response, HttpStatus.BAD_REQUEST, ApiResponse.fail(JwtErrorCode.INVALID_TOKEN.getHttpStatus(), "잘못된 토큰: " + e.getMessage()));
                return;
            } catch (ExpiredJwtException e) {
                log.warn("만료된 토큰: {}", e.getMessage());
                httpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, ApiResponse.fail(JwtErrorCode.TOKEN_EXPIRED.getHttpStatus(), "만료된 토큰: " + e.getMessage()));
                return;
            } catch (UnsupportedJwtException e) {
                log.warn("지원하지 않는 토큰: {}", e.getMessage());  // 지원되지 않는 알고리즘으로 생성된 토큰의 경우
                httpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, ApiResponse.fail(JwtErrorCode.UNSUPPORTED_JWT.getHttpStatus(), "지원하지 않는 토큰: " + e.getMessage()));
                return;
            } catch (IllegalArgumentException e) {
                log.warn("잘못된 토큰 인수: {}", e.getMessage());
                httpResponseUtil.setErrorResponse(response, HttpStatus.BAD_REQUEST, ApiResponse.fail(JwtErrorCode.INVALID_TOKEN.getHttpStatus(), "잘못된 토큰 인수: " + e.getMessage()));
                return;
            } catch (SignatureException e) {
                log.warn("서명 불일치: {}", e.getMessage());
                httpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, ApiResponse.fail(JwtErrorCode.INVALID_SIGNATURE.getHttpStatus(), "서명 불일치: " + e.getMessage()));
                return;
            } catch (Exception e) {
                log.error("예상치 못한 오류 발생: {}", e.getMessage());
                httpResponseUtil.setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, ApiResponse.fail(CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(), "예상치 못한 오류 발생: " + e.getMessage()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String jwtToken) {
        Authentication authentication = jwtUtil.resolveToken(jwtToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}