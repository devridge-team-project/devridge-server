package org.devridge.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomCorsConfig implements WebMvcConfigurer {

    @Value("${devridge.cors.allowedOrigins}")
    private String[] ALLOWED_ORIGINS;

    @Value("${devridge.cors.allowedMethods}")
    private String[] ALLOWED_METHODS;

    @Value("${devridge.cors.allowedHeaders}")
    private String[] ALLOWED_HEADERS;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ALLOWED_ORIGINS)
                .allowedMethods(ALLOWED_METHODS)
                .allowedHeaders(ALLOWED_HEADERS)
                .allowCredentials(true)
                .maxAge(1800);
    }
}
