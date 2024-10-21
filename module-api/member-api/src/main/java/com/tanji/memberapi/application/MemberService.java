package com.tanji.memberapi.application;

import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryService memberQueryService;

    public Member findById(Long memberId) {
        return memberQueryService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found")); // Handle not found case
    }
}
