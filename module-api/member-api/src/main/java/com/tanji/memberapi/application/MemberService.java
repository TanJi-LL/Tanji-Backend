package com.tanji.memberapi.application;

import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import com.tanji.domainredis.util.RedisUtil;
import com.tanji.memberapi.dto.GetImpactSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.tanji.authapi.exception.AuthErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberQueryService memberQueryService;
    private final RedisUtil redisUtil;

    public Member findById(Long memberId) {
        return memberQueryService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found")); // Handle not found case
    }

    public GetImpactSummaryResponse getMemberImpactSummary(Long memberId) {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(MEMBER_NOT_FOUND));

        String key = "ranking:delete";
        String value = "member:" + member.getId();

        Double currentScore = redisUtil.getZSetScore(key, value);

        int carbonsSaved = 0;
        if (currentScore == null) {
            redisUtil.saveToZSet("ranking:delete", "member:" + member.getId(), 0);
        } else {
            carbonsSaved = (int) (currentScore * 4); // 이메일 하나당 4g 탄소 절약
        }

        long rank = redisUtil.getZSetReverseRank(key, value) + 1;

        return GetImpactSummaryResponse.toDto(member.getNickname(), rank, carbonsSaved);
    }
}
