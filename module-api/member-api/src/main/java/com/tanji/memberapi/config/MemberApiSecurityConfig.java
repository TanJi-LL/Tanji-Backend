package com.tanji.memberapi.config;

import com.tanji.authapi.application.CustomOAuth2UserService;
import com.tanji.authapi.config.BaseSecurityConfig;
import com.tanji.authapi.handler.JwtAccessDeniedHandler;
import com.tanji.authapi.handler.JwtAuthenticationEntryPoint;
import com.tanji.authapi.handler.OAuth2AuthenticationFailureHandler;
import com.tanji.authapi.handler.OAuth2AuthenticationSuccessHandler;
import com.tanji.authapi.jwt.JwtAuthenticationFilter;
import com.tanji.authapi.jwt.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MemberApiSecurityConfig extends BaseSecurityConfig {

    public MemberApiSecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
            OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
            JwtExceptionFilter jwtExceptionFilter,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        super(customOAuth2UserService, oAuth2AuthenticationSuccessHandler, oAuth2AuthenticationFailureHandler, jwtExceptionFilter, jwtAuthenticationFilter, jwtAuthenticationEntryPoint, jwtAccessDeniedHandler);
    }

    @Bean(name = "memberApiSecurityFilterChain")
    public SecurityFilterChain memberApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        // Member API에서 허용할 URL 설정
                        .requestMatchers("/api/v1/members/**", "/actuator/health").permitAll() // Member API 경로
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                );

        return http.build();
    }
}
