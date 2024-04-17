package org.devridge.api.domain.community.dto.request;

import java.util.List;
import lombok.Getter;
import org.devridge.api.domain.community.entity.StudyCategory;

@Getter
public class StudyRequest {

    private String title;
    private String content;
    private StudyCategory category;
    private List<String> images;
    private String location;
}
