package org.devridge.api.domain.community.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommunityDetailResponse {

    // 작성자 닉네임, 글제목, 내용, 뷰, 생성시간, 수정시간
    private String nickName;

    private String title;

    private String content;

    private Long views;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String profileImageUrl;

    private String introduction;

    private Long likeCount;

    private Long dislikeCount;
}
