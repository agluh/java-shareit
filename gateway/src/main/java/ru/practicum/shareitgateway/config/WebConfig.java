package ru.practicum.shareitgateway.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.shareitgateway.security.HeaderUserIdArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.auth-header}")
    private String header;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new HeaderUserIdArgumentResolver(header));
    }
}
