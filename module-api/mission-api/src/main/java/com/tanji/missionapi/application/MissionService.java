package com.tanji.missionapi.application;

import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.authapi.exception.AuthErrorCode;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import com.tanji.domainredis.util.RedisUtil;
import com.tanji.missionapi.dto.MissionStatus;
import com.tanji.missionapi.dto.response.TodayMissionStatusesResponse;
import com.tanji.missionapi.enums.TanjiMission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionService {

    private final RedisUtil redisUtil;
    private final MemberQueryService memberQueryService;
    private static final String MISSIONS_PREFIX = "missions:";

    public TodayMissionStatusesResponse getTodayMissionStatuses(Long memberId) {
        Member member = memberQueryService.findById(memberId)
                .orElseThrow(() -> new AuthCustomException(AuthErrorCode.MEMBER_NOT_FOUND));

        String missionStatusKey = MISSIONS_PREFIX + LocalDate.now() + ":" + member.getId();
        List<MissionStatus> missionStatuses = (List<MissionStatus>) redisUtil.get(missionStatusKey);

        if (missionStatuses == null) {
            // Redis에서 오늘의 미션 리스트 조회
            List<TanjiMission> missions = (List<TanjiMission>) redisUtil.get(MISSIONS_PREFIX + LocalDate.now());

            if (missions == null) {
                // 오늘의 미션 리스트가 없는 경우 오늘의 미션 생성
                refreshMissions();
                missions = (List<TanjiMission>) redisUtil.get(MISSIONS_PREFIX + LocalDate.now());
            }

            List<MissionStatus> newMissionStatuses = new ArrayList<>();

            missions.forEach(mission -> newMissionStatuses.add(new MissionStatus(mission.ordinal(), mission.getName(), false)));
            missionStatuses = newMissionStatuses;

            // 자정을 만료시간으로 오늘의 미션 달성 상태 Redis에 저장
            redisUtil.saveAsMidnightTTL(missionStatusKey, missionStatuses);
        }

        return TodayMissionStatusesResponse.toDto(missionStatuses);
    }

    public void refreshMissions() {
        LocalDate now = LocalDate.now();

        // 미션을 랜덤으로 3개 가져옴
        List<TanjiMission> missions = TanjiMission.getRandomMissions(3);

        // 자정을 만료시간으로 오늘의 미션 Redis에 저장
        redisUtil.saveAsMidnightTTL(MISSIONS_PREFIX + now, missions);
    }
}
