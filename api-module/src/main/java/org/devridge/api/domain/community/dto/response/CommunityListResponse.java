package org.devridge.api.domain.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommunityListResponse {

    private Long id;
    private String title;
    private Long views;
    private Long likes;
    private Long comments;
}
