package org.devridge.api.domain.communitycomment;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCommentResponse {

    public CommunityCommentResponse() {
    }

    public CommunityCommentResponse(String nickName, LocalDateTime updatedAt) {
        this.nickName = nickName;
        this.updatedAt = updatedAt;
    }

    public String nickName;

    public LocalDateTime updatedAt;
}
