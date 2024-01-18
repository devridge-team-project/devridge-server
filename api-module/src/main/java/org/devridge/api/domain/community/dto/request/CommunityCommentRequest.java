package org.devridge.api.domain.community.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCommentRequest {

    public CommunityCommentRequest() {
    }

    public CommunityCommentRequest(String content) {
        this.content = content;
    }

    String content;
}
