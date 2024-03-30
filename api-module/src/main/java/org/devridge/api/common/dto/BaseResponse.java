package org.devridge.api.common.dto;

import lombok.Getter;

@Getter
public class BaseResponse<T> {

    private int code;
    private String message;
    private T body;

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.body = data;
    }
}
