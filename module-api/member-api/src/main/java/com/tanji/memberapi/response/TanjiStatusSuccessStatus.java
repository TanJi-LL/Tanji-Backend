package com.tanji.memberapi.response;

import com.tanji.commonmodule.response.BaseSuccessStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TanjiStatusSuccessStatus implements BaseSuccessStatus {
    GET_IMPACT_SUMMARY_SUCCESS(HttpStatus.OK, "절약한 탄소량 및 랭킹 조회 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public int getStatusCode() {
        return this.httpStatus.value();
    }
}