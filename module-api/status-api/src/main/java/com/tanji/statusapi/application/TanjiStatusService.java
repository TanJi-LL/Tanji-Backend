package com.tanji.statusapi.application;

import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.authapi.exception.AuthErrorCode;
import com.tanji.mailapi.application.GmailFetchService;
import com.tanji.statusapi.dto.response.GetTanjiStatusResponse;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import com.tanji.domainredis.util.RedisUtil;
import com.tanji.statusapi.dto.response.GetWaterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TanjiStatusService {
    private final RedisUtil redisUtil;
    private final MemberQueryService memberQueryService;
    private final GmailFetchService gmailFetchService;

    public GetTanjiStatusResponse getTanjiStatus(Long memberId) {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));
        String key = "member:" + member.getId() + ":status";

        // Redis에서 상태 맵 가져오기
        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(key);

        // 상태 맵이 없는 경우 예외 처리 또는 기본값 설정
        if (statusMap == null) {
            throw new IllegalStateException("상태 정보를 찾을 수 없습니다.");
        }

        // DTO로 변환
        return GetTanjiStatusResponse.toDto(
                statusMap.getOrDefault("hungry", 0),
                statusMap.getOrDefault("thirsty", 0),
                statusMap.getOrDefault("feed", 0),
                statusMap.getOrDefault("water", 0)
        );
    }

    public GetTanjiStatusResponse feedTanji(Long memberId) {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));
        String key = "member:" + member.getId() + ":status";

        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(key);

        int currentFeed = statusMap.getOrDefault("feed", 0);
        int currentHungry = statusMap.getOrDefault("hungry", 0);

        // 먹이 수량이 0이면 예외 처리 (또는 다른 방식으로 반환 가능)

        statusMap.put("feed", currentFeed - 1);
        statusMap.put("hungry", Math.min(currentHungry + 25, 100)); // 배고픔 최대치는 100으로 제한

        redisUtil.saveAsPermanentValue(key, statusMap);

        return GetTanjiStatusResponse.toDto(
                statusMap.getOrDefault("hungry", 0),
                statusMap.getOrDefault("thirsty", 0),
                statusMap.getOrDefault("feed", 0),
                statusMap.getOrDefault("water", 0)
        );
    }

    public GetWaterResponse refreshWater(Long memberId) throws GeneralSecurityException, IOException {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));

//        int trashHistoryCount = gmailFetchService.getTrashHistoryCount(member);
        int trashHistoryCount = gmailFetchService.getTrashHistoryCount(memberId);

        String key = "member:" + member.getId() + ":status";
        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(key);

        int currentFeed = statusMap.getOrDefault("water", 0);
        statusMap.put("water", currentFeed + trashHistoryCount);

        redisUtil.saveAsPermanentValue(key, statusMap);

        return new GetWaterResponse(statusMap.getOrDefault("water", 0));
    }
}
