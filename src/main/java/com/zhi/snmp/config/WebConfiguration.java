package com.zhi.snmp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //"http://localhost:9528", "http://localhost:6789", "http://localhost:7890",
        registry.addMapping("/**")   //所有方法都做处理跨域
                .allowedOrigins(
                        "*")  //允许跨域的请求头
                .allowedMethods("*")  //润许通过地请求方法
                .allowedHeaders("*");  //允许的请求头
    }
}

