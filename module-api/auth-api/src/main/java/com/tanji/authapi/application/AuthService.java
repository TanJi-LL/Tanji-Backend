package com.tanji.authapi.application;

import com.tanji.authapi.dto.request.ReissueTokenRequest;
import com.tanji.authapi.dto.response.JwtResponse;
import com.tanji.authapi.exception.JwtErrorCode;
import com.tanji.authapi.exception.JwtCustomException;
import com.tanji.authapi.jwt.JwtUtil;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.dto.response.MemberInfoResponse;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;

    public JwtResponse reissueToken(ReissueTokenRequest reissueTokenRequest) {
        return jwtUtil.reissueToken(reissueTokenRequest.refreshToken());
    }
}
