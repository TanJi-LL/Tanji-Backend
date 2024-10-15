package com.tanji.domainrds.domains.member.dto.response;

import com.tanji.domainrds.domains.member.domain.Member;

public record MemberInfoResponse(
        Long memberId,
        String email,
        String provider,
        String nickname
) {
    public static MemberInfoResponse fromMember(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getProvider(),
                member.getNickname()
        );
    }
}
