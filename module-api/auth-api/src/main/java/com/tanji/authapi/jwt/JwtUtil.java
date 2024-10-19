package com.tanji.authapi.jwt;

import com.tanji.authapi.dto.response.JwtResponse;
import com.tanji.authapi.exception.JwtCustomException;
import com.tanji.authapi.exception.JwtErrorCode;
import com.tanji.authapi.application.CustomUserDetailsService;
import com.tanji.domainredis.util.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tanji.authapi.jwt.JwtProperties.AUTHORITIES;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProperties jwtProperties;
    private final RedisUtil redisUtil;
    private SecretKey secretKey;

    @PostConstruct
    public void afterPropertiesSet() {
        byte[] decoded = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        this.secretKey = Keys.hmacShaKeyFor(decoded);
    }

    public String parseToken(HttpServletRequest request) {
        String header = request.getHeader(JwtProperties.AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(JwtProperties.BEARER_PREFIX)) {
            return null;
        } else {
            return header.split(" ")[1];
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰: {}", e.getMessage());
            throw new JwtCustomException(JwtErrorCode.TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            log.warn("잘못된 토큰: {}", e.getMessage());
            throw new JwtCustomException(JwtErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 토큰: {}", e.getMessage());
            throw new JwtCustomException(JwtErrorCode.UNSUPPORTED_JWT);
        } catch (SignatureException e) {
            log.warn("서명 불일치: {}", e.getMessage());
            throw new JwtCustomException(JwtErrorCode.INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 토큰 인수: {}", e.getMessage());
            throw new JwtCustomException(JwtErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예상치 못한 오류 발생: " + e.getMessage());
        }
    }

    public boolean validateTokenV2(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 예외 처리 로직을 필터로 이동하였으므로, 여기서는 예외를 그대로 던집니다.
            throw e;
        }
    }



    public JwtResponse issueToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        Date accessExpiration = Date.from(issuedAt.plus(jwtProperties.getAccessExpiredTime(), ChronoUnit.MILLIS));
        Date refreshExpiration = Date.from(issuedAt.plus(jwtProperties.getRefreshExpiredTime(), ChronoUnit.MILLIS));
        Date issuedDate = new Date();
        String memberId = authentication.getName();
        var accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedDate)
                .setExpiration(accessExpiration)
                .setSubject(memberId)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .claim(AUTHORITIES, authorities)
                .compact();

        var refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedDate)
                .setExpiration(refreshExpiration)
                .setSubject(memberId)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .claim(AUTHORITIES, authorities)
                .compact();

        redisUtil.saveAsValue(memberId, refreshToken, jwtProperties.getRefreshExpiredTime(), TimeUnit.MILLISECONDS);
        return new JwtResponse(accessToken, refreshToken);
    }

    public Authentication resolveToken(String token) {

        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<SimpleGrantedAuthority> authorities = Stream.of(
                        String.valueOf(claims.get(AUTHORITIES)).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = // 반환 결과 memberId를 바탕으로 유저 정보를 담은 UserDetails객체(Principal 객체) 반환
                customUserDetailsService.loadUserByUsername(
                    claims.getSubject() // JWT에 유저 식별자(Subject)로 MemberId를 담았음
                );
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public JwtResponse reissueToken(String refreshToken) {
        // Refresh Token 유효성 검증 (1차 검증)
        validateTokenV2(refreshToken);

        // Redis에서 저장된 Refresh Token과 비교 (2차 검증)
        Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(refreshToken).getBody();
        String memberId = claims.getSubject();
        String storedRefreshToken = (String) redisUtil.get(memberId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new JwtCustomException(JwtErrorCode.INVALID_TOKEN);
        }

        // 재발급 후 redis에도 반영
        JwtResponse jwtResponse = issueToken(resolveToken(refreshToken));
        redisUtil.saveAsValue(memberId, jwtResponse.refreshToken(), jwtProperties.getRefreshExpiredTime(), TimeUnit.MILLISECONDS);
        return jwtResponse;
    }

}