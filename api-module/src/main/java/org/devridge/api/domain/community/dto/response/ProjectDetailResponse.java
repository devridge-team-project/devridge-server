package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectDetailResponse {

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
    private MemberInfoResponse memberInfoResponse;

    private String category;
    private List<String> skills;
    private String meeting;

}
