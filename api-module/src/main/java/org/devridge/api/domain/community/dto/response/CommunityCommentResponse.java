package org.devridge.api.domain.community.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class CommunityCommentResponse {

    private String nickName;

    private LocalDateTime updatedAt;

    private String content;
}
