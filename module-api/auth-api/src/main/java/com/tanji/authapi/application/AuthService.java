package com.tanji.authapi.application;

import com.tanji.authapi.exception.JwtErrorCode;
import com.tanji.authapi.exception.JwtCustomException;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.dto.response.MemberInfoResponse;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberQueryService memberQueryService;
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberQueryService.findById(memberId).orElseThrow(() -> new JwtCustomException(JwtErrorCode.NOT_FOUND_MEMBER));
        return MemberInfoResponse.fromMember(member);
    }
}
