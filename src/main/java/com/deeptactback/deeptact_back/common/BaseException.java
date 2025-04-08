package com.deeptactback.deeptact_back.common;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final BaseResponseStatus errorCode;

    public BaseException(BaseResponseStatus errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Getter
    private BaseResponseStatus status = null;

    public int getCode() {
        return status.getCode(); // 상태 코드 반환
    }

    public String getMessage() {
        return status.getMessage(); // 메시지 반환
    }
}
