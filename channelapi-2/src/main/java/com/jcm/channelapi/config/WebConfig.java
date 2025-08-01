package com.jcm.channelapi.config;

import com.jcm.common.context.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserContextInterceptor userContextInterceptor;
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    public WebConfig(UserContextInterceptor userContextInterceptor) {
        logger.info("(@JCM) WebConfig constructor");
        this.userContextInterceptor = userContextInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        logger.info("(@JCM) addInterceptors");
        registry.addInterceptor(userContextInterceptor);
    }
}