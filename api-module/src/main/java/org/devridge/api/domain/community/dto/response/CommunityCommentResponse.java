package org.devridge.api.domain.community.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCommentResponse {

    public CommunityCommentResponse() {
    }

    public CommunityCommentResponse(String nickName, LocalDateTime updatedAt, String content) {
        this.nickName = nickName;
        this.updatedAt = updatedAt;
        this.content = content;
    }

    private String nickName;

    private LocalDateTime updatedAt;

    private String content;
}
