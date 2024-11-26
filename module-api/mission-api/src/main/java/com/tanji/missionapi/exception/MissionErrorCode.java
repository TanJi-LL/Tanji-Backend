package com.tanji.missionapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@RequiredArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {
    MISSION_STATUS_NOT_FOUND(NOT_FOUND, "미션 달성 상태를 찾을 수 없습니다."),
    MISSION_ALREADY_COMPLETED(BAD_REQUEST, "이미 달성한 미션입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ResponseEntity<ApiResponse<Void>> toResponseEntity() {
        return ApiResponse.fail(httpStatus, message);
    }
}
