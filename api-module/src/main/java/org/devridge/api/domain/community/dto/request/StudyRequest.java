package org.devridge.api.domain.community.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class StudyRequest {

    private String title;
    private String content;
    private String category;
    private List<String> images;
    private String location;
    private Integer totalPeople;
    private Integer currentPeople;
}
