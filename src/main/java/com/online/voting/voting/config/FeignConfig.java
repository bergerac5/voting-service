package com.online.voting.voting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    //
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();

            if (attributes != null) {

                String token = attributes.getRequest().getHeader("Authorization");

                System.out.println("➡️ FEIGN TOKEN: " + token);

                if (token != null && !token.isEmpty()) {
                    requestTemplate.header("Authorization", token);
                } else {
                    System.out.println("❌ NO AUTH HEADER FOUND IN REQUEST");
                }

            } else {
                System.out.println("❌ NO REQUEST CONTEXT (async/thread issue)");
            }
        };
    }

    // Helper method to get the current JWT token from the request context
    private String getCurrentJwtToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null)
            return null;

        HttpServletRequest request = attributes.getRequest();
        return request.getHeader("Authorization"); // returns "Bearer <token>"
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
