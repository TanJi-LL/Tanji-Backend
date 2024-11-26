package com.tanji.mailapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.exception.CustomException;

public class MailCustomException extends CustomException {
    public MailCustomException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
