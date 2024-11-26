package com.tanji.missionapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanji.missionapi.enums.TanjiMission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MissionStatus {

        @Schema(description = "미션 ID", example = "1")
        int missionId;
        @Schema(description = "미션", example = "시크릿 모드 켜기")
        String mission;
        @JsonProperty("complete")
        @Schema(description = "미션 달성 상태", example = "true")
        boolean isComplete;

        @Builder
        public MissionStatus(int missionId, String mission, boolean isComplete) {
                this.missionId = missionId;
                this.mission = mission;
                this.isComplete = isComplete;
        }

        public static MissionStatus toDto(TanjiMission mission) {
                return MissionStatus.builder()
                        .missionId(mission.ordinal())
                        .mission(mission.getName())
                        .isComplete(false)
                        .build();
        }

        public void completeMission() {
                this.isComplete = true;
        }
}
