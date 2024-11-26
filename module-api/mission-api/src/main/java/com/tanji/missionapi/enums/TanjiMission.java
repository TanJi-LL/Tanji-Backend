package com.tanji.missionapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TanjiMission {

    ENABLE_SECRET_MODE("시크릿 모드 켜기"),
    DECREASE_MONITOR_BRIGHTNESS("모니터 밝기 줄이기"),
    ENABLE_POWER_SAVING_MODE("절전 모드 사용하기"),
    DELETE_DOWNLOAD_HISTORY("다운로드 기록 삭제하기"),
    CLEAR_BROWSER_CACHE("브라우저 캐시 삭제하기"),
    ENABLE_DARK_MODE("다크 모드 켜기"),
    ORGANIZE_BOOKMARKS("북마크 정리하기"),
    TURN_OFF_KEYBOARD_BACKLIGHT("키보드 백라이트 끄기"),
    CLEAN_DUPLICATE_FILES("중복된 파일 정리하기");

    private final String name;

    public static List<TanjiMission> getRandomMissions(int count) {
        List<TanjiMission> missions = new ArrayList<>(List.of(TanjiMission.values()));
        Collections.shuffle(missions); // 미션을 랜덤하게 섞음
        return new ArrayList<>(missions.subList(0, Math.min(count, missions.size())));
    }
}
