package com.tanji.statusapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메인페이지에서 탄지 상태 조회 응답 DTO")
public record GetTanjiStatusResponse(
        @Schema(description = "배고픔 수치", example = "50") int hungry,
        @Schema(description = "목마름 수치", example = "50") int thirsty,
        @Schema(description = "남은 먹이 수", example = "3") int feed,
        @Schema(description = "남은 물 수", example = "3") int water
) {
    public static GetTanjiStatusResponse toDto(int hungry, int thirsty, int feed, int water){
        return new GetTanjiStatusResponse(hungry, thirsty, feed, water);
    }
}
