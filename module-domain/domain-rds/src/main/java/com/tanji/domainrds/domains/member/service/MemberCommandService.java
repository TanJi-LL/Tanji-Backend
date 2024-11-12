package com.tanji.domainrds.domains.member.service;

import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

//    public void updateLastHistoryId(Member member, BigInteger lastHistoryId) {
//        member.updateLastHistoryId(lastHistoryId);
//    }
}
