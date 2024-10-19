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
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwtToken = jwtUtil.parseToken(request);
        if (jwtToken != null) {
            try {
                jwtUtil.validateToken(jwtToken);
                setAuthentication(jwtToken);
            } catch (Exception e) {
                throw e;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String jwtToken) {
        Authentication authentication = jwtUtil.resolveToken(jwtToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}