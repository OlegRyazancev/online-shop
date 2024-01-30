package com.ryazancev.apigateway.config;

import com.ryazancev.apigateway.util.exception.GlobalExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ApiGatewayConfig {

    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
