package com.samsungjeomja.dotflow.common.response.exception;


import com.samsungjeomja.dotflow.common.response.code.BaseErrorCode;
import com.samsungjeomja.dotflow.common.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}