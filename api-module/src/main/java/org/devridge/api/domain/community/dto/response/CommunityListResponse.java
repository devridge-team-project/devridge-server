package org.devridge.api.domain.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityListResponse {

    private Long id;
    private String title;
    private Long views;
    private Long likeCount;
    private Long comments;
}
