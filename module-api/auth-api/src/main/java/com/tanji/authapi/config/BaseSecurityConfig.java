package com.tanji.authapi.config;

import com.tanji.authapi.application.CustomOAuth2UserService;
import com.tanji.authapi.handler.OAuth2AuthenticationFailureHandler;
import com.tanji.authapi.jwt.JwtAuthenticationFilter;
import com.tanji.authapi.handler.JwtAccessDeniedHandler;
import com.tanji.authapi.handler.JwtAuthenticationEntryPoint;
import com.tanji.authapi.handler.OAuth2AuthenticationSuccessHandler;
import com.tanji.authapi.jwt.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class BaseSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JdbcTemplate jdbcTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(CorsConfig.apiConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                    "/",
                                    "/oauth2/authorization/**",
                                    "/login/oauth2/code/**",
                                    "/oauth2/**",
                                    "/api/v1/public/**"
                        ).permitAll() // 공개 API 경로
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(c -> c.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring() // 특정 경로를 보안 필터 체인에서 아예 제외
                .requestMatchers("/actuator/health","/error", "/favicon.ico", "/swagger-ui/**", "/api-docs/**",
                        "/api/v1/mails/**");
    }

}

