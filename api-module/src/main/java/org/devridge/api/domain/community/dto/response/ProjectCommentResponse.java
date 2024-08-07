package org.devridge.api.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.devridge.api.common.dto.UserInformation;

@Builder
@Getter
@AllArgsConstructor
public class ProjectCommentResponse {

    @JsonProperty("id")
    private Long commentId;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likes;
    private Long dislikes;

    @JsonProperty("member")
    private UserInformation userInformation;
}
