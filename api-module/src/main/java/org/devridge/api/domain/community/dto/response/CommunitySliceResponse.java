package org.devridge.api.domain.community.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommunitySliceResponse {
    private Long id;
    private String title;
    private String content;
    private Long views;
    private Long likes;
    private Long comments;
    private UserInformation member;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long scraps;
}
