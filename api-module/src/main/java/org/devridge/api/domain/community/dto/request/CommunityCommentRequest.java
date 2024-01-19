package org.devridge.api.domain.community.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommunityCommentRequest {
    
    @NotNull
    String content;
}
