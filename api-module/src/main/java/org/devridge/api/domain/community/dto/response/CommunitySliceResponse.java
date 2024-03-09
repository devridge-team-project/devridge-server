package org.devridge.api.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class CommunitySliceResponse {
    private Long id;
    private String title;
    private Long viewCount;
    private Long likeCount;
    private Long comments;
    private MemberInfoResponse member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<HashtagResponse> hashtags;
    private Long scraps;

    public void setHashtags(List<HashtagResponse> hashtags) {
        this.hashtags = hashtags;
    }
}