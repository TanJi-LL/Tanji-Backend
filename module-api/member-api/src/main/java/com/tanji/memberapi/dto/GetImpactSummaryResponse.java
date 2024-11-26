package com.tanji.memberapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record GetImpactSummaryResponse(
        @Schema(description = "닉네임", example = "탄지")
        String username,
        @Schema(description = "랭킹", example = "124")
        long rank,
        @Schema(description = "절약한 탄소량(g)", example = "20")
        int carbonsSaved,
        @Schema(description = "나무 비유(그루)", example = "0.312")
        float treesSaved,
        @Schema(description = "페트병 비유(병)", example = "10.399")
        float bottlesSaved,
        @Schema(description = "오일 비유(L)", example = "1.132")
        float OilsSaved
) {
        public static GetImpactSummaryResponse toDto(String username, long rank, int carbonsSaved) {
                return new GetImpactSummaryResponse(username,
                        rank,
                        carbonsSaved,
                        Math.round((carbonsSaved / 6600f) * 1000) / 1000f,
                        Math.round((carbonsSaved / 164f) * 1000) / 1000f,
                        Math.round((carbonsSaved / 2670f) * 1000) / 1000f);
        }
}