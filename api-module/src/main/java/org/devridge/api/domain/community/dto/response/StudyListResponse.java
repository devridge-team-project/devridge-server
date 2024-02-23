package org.devridge.api.domain.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StudyListResponse {

    private Long studyId;
    private String category;
    private String title;
    private String content;
    private Long likes;
    private Long dislikes;
    private Long views;
    private String location;
    private Integer totalPeople;
    private Integer currentPeople;
}
