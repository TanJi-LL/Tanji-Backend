package com.tanji.authapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.exception.CustomException;

public class AuthCustomException extends CustomException {
    public AuthCustomException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
