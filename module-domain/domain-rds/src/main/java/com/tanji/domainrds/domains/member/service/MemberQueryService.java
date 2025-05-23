package com.tanji.domainrds.domains.member.service;

import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {
    private final MemberRepository memberRepository;
    public Optional<Member> findById(Long socialId) {
        return memberRepository.findById(socialId);
    }
    public Optional<Member> findBySocialId(String socialId) {
        return memberRepository.findBySocialId(socialId);
    }
}
