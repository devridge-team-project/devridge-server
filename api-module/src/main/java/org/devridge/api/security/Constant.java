package org.devridge.api.security;

public interface Constant {

    String USER_ROLE = "ROLE_USER";

    String[] ALL_PERMIT_PATHS = new String[]{
            "/api/users/one",
            "/api/login",
            "/api/users/emails/availability",
            "/api/login/**",
            "/api/social-login",
            "/api/social-login/**",
            "/api/emails/send-verification-email",
            "/api/emails/verify-code",
    };

    String[] USER_ROLE_PERMIT_PATHS = new String[]{
            "/api/**",
    };

}
