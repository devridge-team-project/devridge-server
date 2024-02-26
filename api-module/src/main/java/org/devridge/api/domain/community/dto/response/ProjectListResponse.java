package org.devridge.api.domain.community.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ProjectListResponse {

    private Long id;
    private String category;
    private String title;
    private String content;
    private Long likes;
    private Long dislikes;
    private Long view;
    private Boolean isRecruiting;
    private List<String> skills;
    private String meeting;
}
