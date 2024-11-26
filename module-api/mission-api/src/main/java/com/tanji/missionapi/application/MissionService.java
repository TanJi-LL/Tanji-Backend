package com.tanji.missionapi.application;

import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.authapi.exception.AuthErrorCode;
import com.tanji.missionapi.dto.response.CompleteMissionResponse;
import com.tanji.missionapi.exception.MissionCustomException;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import com.tanji.domainredis.util.RedisUtil;
import com.tanji.missionapi.dto.MissionStatus;
import com.tanji.missionapi.dto.response.GetTodayMissionStatusesResponse;
import com.tanji.missionapi.enums.TanjiMission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tanji.missionapi.exception.MissionErrorCode.MISSION_ALREADY_COMPLETED;
import static com.tanji.missionapi.exception.MissionErrorCode.MISSION_STATUS_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionService {

    private final RedisUtil redisUtil;
    private final MemberQueryService memberQueryService;
    private static final String MISSIONS_PREFIX = "missions:";

    public GetTodayMissionStatusesResponse getTodayMissionStatuses(Long memberId) {
        Member member = findMemberById(memberId);

        String missionStatusKey = "member:" + member.getId() + ":missions:" + LocalDate.now();
        List<MissionStatus> missionStatuses = (List<MissionStatus>) redisUtil.get(missionStatusKey);

        if (missionStatuses == null) {
            // Redis에서 오늘의 미션 리스트 조회
            List<TanjiMission> missions = (List<TanjiMission>) redisUtil.get("missions:" + LocalDate.now());

            if (missions == null) {
                // 오늘의 미션 리스트가 없는 경우 오늘의 미션 생성
                refreshMissions();
                missions = (List<TanjiMission>) redisUtil.get("missions:" + LocalDate.now());
            }

            List<MissionStatus> newMissionStatuses = new ArrayList<>();

            missions.forEach(mission -> newMissionStatuses.add(MissionStatus.toDto(mission)));
            missionStatuses = newMissionStatuses;

            // 자정을 만료시간으로 오늘의 미션 달성 상태 Redis에 저장
            redisUtil.saveAsMidnightTTL(missionStatusKey, missionStatuses);
        }

        return GetTodayMissionStatusesResponse.toDto(missionStatuses);
    }

    public void refreshMissions() {
        LocalDate now = LocalDate.now();

        // 미션을 랜덤으로 3개 가져옴
        List<TanjiMission> missions = TanjiMission.getRandomMissions(3);

        // 자정을 만료시간으로 오늘의 미션 Redis에 저장
        redisUtil.saveAsMidnightTTL("missions:" + now, missions);
    }

    public CompleteMissionResponse completeMission(Long memberId, int missionId) {
        Member member = findMemberById(memberId);

        String missionStatusKey = "member:" + member.getId() + ":missions:" + LocalDate.now();
        List<MissionStatus> currentMissionStatuses = (List<MissionStatus>) redisUtil.get(missionStatusKey);;

        if (currentMissionStatuses == null) {
            throw new MissionCustomException(MISSION_STATUS_NOT_FOUND);
        }

        // 미션 달성 상태 완료로 업데이트
        MissionStatus currentMissionStatus = currentMissionStatuses.stream()
                .filter(missionStatus -> missionStatus.getMissionId() == missionId)
                .findFirst()
                .orElseThrow(() -> new MissionCustomException(MISSION_STATUS_NOT_FOUND));

        if (currentMissionStatus.isComplete()) {
            throw new MissionCustomException(MISSION_ALREADY_COMPLETED);
        }

        currentMissionStatus.completeMission();
        redisUtil.saveAsMidnightTTL(missionStatusKey, currentMissionStatuses);

        // 먹이 수 증가
        String tanjiStatusKey = "member:" + member.getId() + ":status";

        Map<String, Integer> statusMap = (Map<String, Integer>) redisUtil.get(tanjiStatusKey);

        int currentFeed = statusMap.getOrDefault("feed", 0);
        statusMap.put("feed", currentFeed + 1);

        redisUtil.saveAsPermanentValue(tanjiStatusKey, statusMap);

        return CompleteMissionResponse.toDto(statusMap.get("feed"));
    }

    private Member findMemberById(Long memberId) {
        return memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));
    }
}
