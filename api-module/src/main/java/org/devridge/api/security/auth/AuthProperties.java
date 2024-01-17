package org.devridge.api.security.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthProperties {
    private static String accessSecret;
    private static String refreshSecret;

    @Value("${devridge.jwt.secret.access}")
    private void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public static String getAccessSecret() {
        return accessSecret;
    }

    @Value("${devridge.jwt.secret.refresh}")
    private void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    public static String getRefreshSecret() {
        return refreshSecret;
    }
}
