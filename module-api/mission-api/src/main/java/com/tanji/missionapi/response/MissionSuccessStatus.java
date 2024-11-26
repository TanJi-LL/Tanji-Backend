package com.tanji.missionapi.response;

import com.tanji.commonmodule.response.BaseSuccessStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MissionSuccessStatus implements BaseSuccessStatus {
    GET_TODAY_MISSION_SUCCESS(HttpStatus.OK, "오늘의 미션 및 달성 여부 조회 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public int getStatusCode() {
        return this.httpStatus.value();
    }
}