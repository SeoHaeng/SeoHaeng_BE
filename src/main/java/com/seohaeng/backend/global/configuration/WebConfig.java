package com.seohaeng.backend.global.configuration;

import com.seohaeng.backend.global.security.handler.AccessTokenArgumentResolver;
import com.seohaeng.backend.global.security.handler.AuthUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthUserArgumentResolver authUserArgumentResolver;
    private final AccessTokenArgumentResolver accessTokenArgumentResolver;

    public WebConfig(
            AuthUserArgumentResolver authUserArgumentResolver,
            AccessTokenArgumentResolver accessTokenArgumentResolver
    ) {
        this.authUserArgumentResolver = authUserArgumentResolver;
        this.accessTokenArgumentResolver = accessTokenArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
        resolvers.add(accessTokenArgumentResolver);
    }
}