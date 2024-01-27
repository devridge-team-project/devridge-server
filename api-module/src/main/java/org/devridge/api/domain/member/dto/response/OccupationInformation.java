package org.devridge.api.domain.member.dto.response;

import lombok.Getter;

@Getter
public class OccupationInformation {

    private Long id;
    private String occupationName;

    public OccupationInformation(Long id, String occupationName) {
        this.id = id;
        this.occupationName = occupationName;
    }
}
