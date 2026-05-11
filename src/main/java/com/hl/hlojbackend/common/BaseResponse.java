package com.hl.hlojbackend.common;

import com.hl.hlojbackend.exception.ErrorCode;
import lombok.Data;

/**
 * 通用返回类
 */
@Data
public class BaseResponse<T> {

    private int code;

    private String message;

    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, String message) {
        this(code, message, null);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public BaseResponse(ErrorCode errorCode, T data) {
        this(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public BaseResponse(ErrorCode errorCode, String message, T data) {
        this(errorCode.getCode(), message, data);
    }

    public BaseResponse(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), message, null);
    }

}
