package com.tanji.missionapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오늘의 미션 달성 상태 업데이트 응답 DTO")
public record CompleteMissionResponse(
        @Schema(description = "남은 먹이 수", example = "10")
        int feed
) {
    public static CompleteMissionResponse toDto(int feed){
        return new CompleteMissionResponse(feed);
    }
}
