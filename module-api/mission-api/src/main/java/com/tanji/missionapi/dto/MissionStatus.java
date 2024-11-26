package com.tanji.missionapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MissionStatus(
        @Schema(description = "미션 ID", example = "1")
        Integer missionId,
        @Schema(description = "미션", example = "시크릿 모드 켜기")
        String mission,
        @Schema(description = "미션 달성 여부", example = "true")
        Boolean isComplete
) {
}
