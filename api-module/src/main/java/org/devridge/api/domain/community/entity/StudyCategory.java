package org.devridge.api.domain.community.entity;

public enum StudyCategory {
    CODING_TEST_STUDY("코테 스터디"),
    CS_STUDY("CS 스터디"),
    INTERVIEW_STUDY("면접 스터디"),
    ETC("기타");

    private String value;

    StudyCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
