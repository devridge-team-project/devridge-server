package org.devridge.api.domain.community.entity;

public enum ProjectRole {

    frontEnd,
    backEnd,
    PM,
    design;

    public static boolean isValidRole(String roleName) {
        for (ProjectRole role : ProjectRole.values()) {
            if (role.name().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
