package org.devridge.api.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudyListResponse {

    private Long id;
    private String category;
    private String title;
    private String content;
    private String images;
    private Long likes;
    private Long dislikes;
    private Long views;
    private String location;
    private Integer totalPeople;
    private Integer currentPeople;
}
