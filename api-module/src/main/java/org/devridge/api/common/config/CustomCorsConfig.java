package org.devridge.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomCorsConfig implements WebMvcConfigurer {

    @Value("${devridge.cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Value("${devridge.cors.allowedMethods}")
    private String[] allowedMethods;

    @Value("${devridge.cors.allowedHeaders}")
    private String[] allowedHeaders;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .allowCredentials(true)
                .maxAge(1800);
    }
}
