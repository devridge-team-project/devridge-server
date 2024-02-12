package org.devridge.common.dto;

import lombok.Getter;

@Getter
public class BaseErrorResponse {

    private String error;

    public BaseErrorResponse(String error) {
        this.error = error;
    }
}
