package com.tanji.mailapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum MailErrorCode implements BaseErrorCode {
    MAIL_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 정보를 가져오는 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ResponseEntity<ApiResponse<Void>> toResponseEntity() {
        return ApiResponse.fail(httpStatus, message);
    }
}
