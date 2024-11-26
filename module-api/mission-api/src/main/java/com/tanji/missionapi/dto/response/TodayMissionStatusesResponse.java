package com.tanji.missionapi.dto.response;

import com.tanji.missionapi.dto.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘의 미션 및 달성 여부 조회 응답 DTO")
public record TodayMissionStatusesResponse(
        @Schema(description = "오늘의 미션 및 달성 여부") List<MissionStatus> missionStatuses
) {

    public static TodayMissionStatusesResponse toDto(List<MissionStatus> missionStatuses){
        return new TodayMissionStatusesResponse(missionStatuses);
    }
}