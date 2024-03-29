package org.devridge.api.domain.community.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.devridge.api.domain.community.entity.Meeting;
import org.devridge.api.domain.community.entity.ProjectCategory;
import org.devridge.api.domain.community.validator.ValidateEnum;

@Getter
public class ProjectRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @ValidateEnum(enumClass = ProjectCategory.class, message = "category에 없는 값입니다.")
    private List<String> roles;

    private List<String> images;

    private List<Long> skillIds;

    @ValidateEnum(enumClass = Meeting.class, message = "meeting에 없는 값입니다.")
    private String meeting;

    private Boolean isRecruiting;
}
