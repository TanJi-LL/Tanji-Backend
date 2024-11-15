package com.tanji.statusapi.response;

import com.tanji.commonmodule.response.BaseSuccessStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TanjiStatusSuccessStatus implements BaseSuccessStatus {
    GET_STATUS_SUCCESS(HttpStatus.OK, "탄지 상태 조회 성공"),
    FEED_TANJI_SUCCESS(HttpStatus.OK, "탄지 먹이 주기 및 배고픔 상태 업데이트 성공"),
    REFRESH_WATER_SUCCESS(HttpStatus.OK, "물 상태 업데이트 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public int getStatusCode() {
        return this.httpStatus.value();
    }
}