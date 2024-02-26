package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class CommunityCommentResponse {

    @JsonProperty("id")
    private Long commentId;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long dislikeCount;

    @JsonProperty("member")
    private MemberInfoResponse memberInfoResponse;
}
