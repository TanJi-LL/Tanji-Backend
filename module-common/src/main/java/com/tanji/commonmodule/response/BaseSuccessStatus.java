package com.tanji.commonmodule.response;

import org.springframework.http.HttpStatus;

public interface BaseSuccessStatus {
    HttpStatus getHttpStatus();
    String getMessage();
    int getStatusCode();
}