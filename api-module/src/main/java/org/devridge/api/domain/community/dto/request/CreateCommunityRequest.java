package org.devridge.api.domain.community.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCommunityRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    private List<String> hashtags;
    private List<String> images;
}
