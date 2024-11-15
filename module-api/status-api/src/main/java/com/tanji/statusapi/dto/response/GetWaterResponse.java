package com.tanji.statusapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "남은 물 수 조회 응답 DTO")
public record GetWaterResponse(
        @Schema(description = "남은 물 수", example = "3") int water
) {
    public static GetWaterResponse toDto(int water){
        return new GetWaterResponse(water);
    }
}
