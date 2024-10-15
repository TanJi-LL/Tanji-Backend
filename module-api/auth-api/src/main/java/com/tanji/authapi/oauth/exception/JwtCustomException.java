package com.tanji.authapi.oauth.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.exception.CustomException;
import lombok.Getter;

@Getter
public class JwtCustomException extends CustomException {

    public JwtCustomException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public JwtCustomException(BaseErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
