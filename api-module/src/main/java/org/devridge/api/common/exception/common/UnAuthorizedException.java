package org.devridge.api.common.exception.common;

public class UnAuthorizedException extends BaseException {

    public UnAuthorizedException() {
        super(401, "cannot access to server");
    }
}
