package com.tanji.statusapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum TanjiStatusErrorCode implements BaseErrorCode {

    INSUFFICIENT_WATER(BAD_REQUEST, "남은 물이 부족합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ResponseEntity<ApiResponse<Void>> toResponseEntity() {
        return ApiResponse.fail(httpStatus, message);
    }
}

