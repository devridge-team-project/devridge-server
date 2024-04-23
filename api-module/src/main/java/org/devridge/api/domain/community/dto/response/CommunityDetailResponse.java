package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

@Builder
@Getter
public class CommunityDetailResponse {

    @JsonProperty("id")
    private Long communityId;

    private String title;

    private String content;

    private Long views;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long likes;

    private Long dislikes;

    @JsonProperty("member")
    private UserInformation userInformation;
}
