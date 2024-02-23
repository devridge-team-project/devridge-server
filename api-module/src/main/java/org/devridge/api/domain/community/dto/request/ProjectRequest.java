package org.devridge.api.domain.community.dto.request;

import java.util.List;
import lombok.Getter;
import org.devridge.api.domain.community.entity.ProjectCategory;

@Getter
public class ProjectRequest {

    private String title;
    private String content;
    private ProjectCategory category;
    private List<String> images;

}