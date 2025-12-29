package com.nhnacademy._vidiafront.global.exception;

import lombok.Getter;

@Getter
public class ApiRequestException extends RuntimeException {
    private final int status;
    private final String errorCode;

    public ApiRequestException(int status, String message, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public ApiRequestException(String message) {
        this(500, message, "API_REQUEST_FAILED");
    }
}
