package com.tanji.authapi.member.service;

import com.tanji.authapi.oauth.exception.JwtCustomException;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.dto.response.MemberInfoResponse;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.tanji.authapi.oauth.exception.JwtErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberQueryService memberQueryService;
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberQueryService.findById(memberId).orElseThrow(() -> new JwtCustomException(NOT_FOUND_MEMBER));
        return MemberInfoResponse.fromMember(member);
    }
}
