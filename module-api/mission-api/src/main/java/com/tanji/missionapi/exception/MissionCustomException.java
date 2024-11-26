package com.tanji.missionapi.exception;

import com.tanji.commonmodule.exception.BaseErrorCode;
import com.tanji.commonmodule.exception.CustomException;

public class MissionCustomException extends CustomException {
    public MissionCustomException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
