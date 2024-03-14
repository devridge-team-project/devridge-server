package org.devridge.api.common.security.type;

import org.springframework.beans.factory.annotation.Value;

public class SecurityPathType {

    public static final String USER_ROLE = "USER";

    @Value("${devridge.security.all-permit-paths}")
    public String[] ALL_PERMIT_PATHS;

    @Value("${devridge.security.user-role-permit-paths}")
    public String[] USER_ROLE_PERMIT_PATHS;
}
