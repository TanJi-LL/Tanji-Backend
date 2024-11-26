package com.tanji.statusapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.exception.CustomException;

public class TanjiStatusException extends CustomException {
    public TanjiStatusException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
