package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommunityDetailResponse {

    @JsonProperty("id")
    private Long communityId;

    private String title;
    private String content;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long dislikeCount;

    @JsonProperty("member")
    private MemberInfoResponse memberInfoResponse;

    private List<String> hashtags;
}
