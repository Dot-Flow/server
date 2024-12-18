package com.samsungjeomja.dotflow.common.response.code.status;


import com.samsungjeomja.dotflow.common.response.code.BaseErrorCode;
import com.samsungjeomja.dotflow.common.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 일반 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    _JSON_MAPPING_FAIL(HttpStatus.BAD_REQUEST, "JSON404","Json 매핑 실패"),

    // S3 관련
    _FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "FILE_001", "파일 업로드에 실패했습니다."),
    _FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST, "FILE_002", "파일 삭제에 실패했습니다."),
    _FILE_EXTENSTION_ERROR(HttpStatus.BAD_REQUEST, "FILE_003", "유효하지 않은 파일 확장자입니다."),

    ;

    private final HttpStatus httpStatus;

    private final String code;
    private final String message;


    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }
}