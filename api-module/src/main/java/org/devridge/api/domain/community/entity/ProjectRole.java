package org.devridge.api.domain.community.entity;

public enum ProjectRole {

    프론트엔드,
    백엔드,
    기획,
    디자인;

    public static boolean isValidRole(String roleName) {
        for (ProjectRole role : ProjectRole.values()) {
            if (role.name().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
