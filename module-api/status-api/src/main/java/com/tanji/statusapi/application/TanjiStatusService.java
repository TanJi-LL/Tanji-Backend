package com.tanji.statusapi.application;

import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.authapi.exception.AuthErrorCode;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import com.tanji.domainredis.util.RedisUtil;
import com.tanji.mailapi.application.GmailFetchService;
import com.tanji.statusapi.dto.response.GetTanjiStatusResponse;
import com.tanji.statusapi.dto.response.GetWaterResponse;
import com.tanji.statusapi.exception.TanjiStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import static com.tanji.statusapi.exception.TanjiStatusErrorCode.INSUFFICIENT_WATER;

@Slf4j
@Component
@RequiredArgsConstructor
public class TanjiStatusService {
    private final RedisUtil redisUtil;
    private final MemberQueryService memberQueryService;
    private final GmailFetchService gmailFetchService;

    public GetTanjiStatusResponse getTanjiStatus(Long memberId) throws GeneralSecurityException, IOException {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));
        String key = "member:" + member.getId() + ":status";

//        // 메일 마지막 기록 ID Redis에 업데이트
//        int trashHistoryCount = gmailFetchService.getTrashHistoryCount(member.getId());

        // Redis에서 상태 맵 가져오기
        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(key);

        // 상태 맵이 없는 경우 예외 처리 또는 기본값 설정
        if (statusMap == null) {
            throw new IllegalStateException("상태 정보를 찾을 수 없습니다.");
        }

//        // 물 상태 업데이트
//        int currentFeed = statusMap.getOrDefault("water", 0);
//        statusMap.put("water", currentFeed + trashHistoryCount);
//
//        redisUtil.saveAsPermanentValue(key, statusMap);

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

        // 메일 마지막 기록 ID Redis에 업데이트
        int trashHistoryCount = gmailFetchService.getTrashHistoryCount(memberId);

        String statusKey = "member:" + member.getId() + ":status";
        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(statusKey);

        // 물 상태 업데이트
        int currentFeed = statusMap.getOrDefault("water", 0);
        statusMap.put("water", currentFeed + trashHistoryCount);
        redisUtil.saveAsPermanentValue(statusKey, statusMap);

        // 누적 메일 삭제 수 업데이트
        String countKey = "member:" + member.getId() + ":delete:count";
        Integer currentCount = (Integer) redisUtil.get(countKey);

        if (currentCount == null) {
            currentCount = 0;
        }
        redisUtil.saveAsPermanentValue(countKey, currentCount + trashHistoryCount);

        return new GetWaterResponse(statusMap.getOrDefault("water", 0));
    }

    public GetTanjiStatusResponse waterTanji(Long memberId) {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));
        String key = "member:" + member.getId() + ":status";

        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(key);

        int currentWater = statusMap.getOrDefault("water", 0);
        int currentThirsty = statusMap.getOrDefault("thirsty", 0);

        if (currentWater <= 0) {
            throw new TanjiStatusException(INSUFFICIENT_WATER);
        }

        statusMap.put("water", currentWater - 1);
        statusMap.put("thirsty", Math.min(currentThirsty + 15, 100)); // 목마름 최대치는 100으로 제한

        redisUtil.saveAsPermanentValue(key, statusMap);

        return GetTanjiStatusResponse.toDto(
                statusMap.getOrDefault("hungry", 0),
                statusMap.getOrDefault("thirsty", 0),
                statusMap.getOrDefault("feed", 0),
                statusMap.getOrDefault("water", 0)
        );
    }
}
