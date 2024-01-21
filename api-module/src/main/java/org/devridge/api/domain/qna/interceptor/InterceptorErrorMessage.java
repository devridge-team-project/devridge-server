package org.devridge.api.domain.qna.interceptor;

import lombok.Getter;

@Getter
public class InterceptorErrorMessage {

    private String error;

    public InterceptorErrorMessage(String error) {
        this.error = error;
    }
}
