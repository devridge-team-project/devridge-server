package org.devridge.api.domain.community;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityDto {

    public CommunityDto() {

    }
    public CommunityDto(Community community) {
        this.title = community.getTitle();
        this.content = community.getContent();
        this.id = community.getId();
        this.memberId = community.getMemberId();
        this.isDeleted = community.getIsDeleted();
        this.views = community.getViews();
        this.createdAt = community.getCreatedAt();
        this.updatedAt = community.getUpdatedAt();
    }

    @JsonProperty("title")
    String title;

    @JsonProperty("content")
    String content;

    Long id;

    Long memberId;

    Boolean isDeleted;

    Long views;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}

