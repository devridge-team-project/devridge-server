package org.devridge.api.domain.community.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommunityDetailResponse {

    // 작성자 닉네임, 글제목, 내용, 뷰, 생성시간, 수정시간
    private String nickName;

    private String title;

    private String content;

    private Long views;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public CommunityDetailResponse(String nickName, String title, String content, Long views, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.nickName = nickName;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
