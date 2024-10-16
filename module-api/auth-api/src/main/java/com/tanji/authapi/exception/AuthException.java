package com.tanji.authapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.exception.CustomException;

public class AuthException extends CustomException {
    public AuthException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
