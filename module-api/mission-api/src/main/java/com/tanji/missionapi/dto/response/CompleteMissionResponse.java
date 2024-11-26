package com.tanji.missionapi.dto.response;

public record CompleteMissionResponse(
    int feed
) {
    public static CompleteMissionResponse toDto(int feed){
        return new CompleteMissionResponse(feed);
    }
}
