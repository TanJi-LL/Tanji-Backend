package com.tanji.commonmodule.exception;

import com.tanji.commonmodule.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();
    String getMessage();

    ResponseEntity<ApiResponse<Void>> toResponseEntity();
}