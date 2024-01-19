package org.devridge.api.config;

import lombok.RequiredArgsConstructor;
import org.devridge.api.domain.qna.interceptor.QnAAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebServiceConfig implements WebMvcConfigurer {

    private final QnAAuthInterceptor qnaAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(qnaAuthInterceptor)
                .addPathPatterns("/api/qna/**");
    }
}