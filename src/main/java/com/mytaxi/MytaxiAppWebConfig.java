package com.mytaxi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mytaxi.util.LoggingInterceptor;

/**
 * 
 * Web Configurations
 *
 */
@Configuration
public class MytaxiAppWebConfig implements WebMvcConfigurer
{

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
    }

}
