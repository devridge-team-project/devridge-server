package org.devridge.api.domain.qna.dto.request;

import lombok.Getter;
import org.devridge.api.domain.qna.dto.type.LikeStatus;
import org.devridge.api.domain.qna.validator.ValidateLikeStatus;

@Getter
public class CreateLikeOrDislikeRequest {

    @ValidateLikeStatus(enumClass = LikeStatus.class)
    private String likeStatus;
}
