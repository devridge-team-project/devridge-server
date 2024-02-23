package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StudyDetailResponse {

    @JsonProperty("id")
    private Long studyId;

    private String title;
    private String content;
    private Long views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likes;
    private Long dislikes;
    private String location;
    private Integer totalPeople;
    private Integer currentPeople;

    @JsonProperty("member")
    private MemberInfoResponse memberInfoResponse;

}
