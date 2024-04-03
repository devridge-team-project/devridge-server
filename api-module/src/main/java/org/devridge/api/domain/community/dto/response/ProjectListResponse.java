package org.devridge.api.domain.community.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListResponse {

    private Long id;
    private String roles;
    private String title;
    private String content;
    private Long likes;
    private Long dislikes;
    private Long views;
    private Boolean isRecruiting;
    private List<SkillResponse> skills;
    private String meeting;

    public void setSkills(List<SkillResponse> skills) {
        this.skills = skills;
    }
}
