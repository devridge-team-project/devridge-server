package org.devridge.api.domain.community.exception;

import org.devridge.api.exception.common.BaseException;

public class MyCommunityForbiddenException extends BaseException {

    public MyCommunityForbiddenException(int code, String message) {
        super(code, message);
    }
}
