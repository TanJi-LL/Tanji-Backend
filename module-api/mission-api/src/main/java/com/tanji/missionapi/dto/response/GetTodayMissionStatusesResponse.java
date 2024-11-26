package com.tanji.missionapi.dto.response;

import com.tanji.missionapi.dto.MissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "오늘의 미션 및 달성 상태 조회 응답 DTO")
public record GetTodayMissionStatusesResponse(
        @Schema(description = "오늘의 미션 및 달성 상태") List<MissionStatus> missionStatuses
) {

    public static GetTodayMissionStatusesResponse toDto(List<MissionStatus> missionStatuses){
        return new GetTodayMissionStatusesResponse(missionStatuses);
    }
}