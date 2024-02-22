package org.devridge.api.domain.community.entity;

public enum ProjectCategory {
    TOY_PROJECT("토이 프로젝트"),
    PORTFOLIO_PROJECT("포트폴리오 프로젝트"),
    ETC("기타");

    private String value;

    ProjectCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
