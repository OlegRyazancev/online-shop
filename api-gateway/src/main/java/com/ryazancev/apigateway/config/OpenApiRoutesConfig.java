package com.ryazancev.apigateway.config;

import com.ryazancev.apigateway.util.exception.GlobalExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
public class OpenApiRoutesConfig {

    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        return builder
                .routes()
                .route(r -> r
                        .path("/v3/api-docs/auth")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://AUTH"))
                .route(r -> r
                        .path("/v3/api-docs/customer")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://CUSTOMER"))
                .route(r -> r
                        .path("/v3/api-docs/organization")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://ORGANIZATION"))
                .route(r -> r
                        .path("/v3/api-docs/product")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://PRODUCT"))
                .route(r -> r
                        .path("/v3/api-docs/admin")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://ADMIN"))
                .route(r -> r
                        .path("/v3/api-docs/purchase")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://PURCHASE"))
                .route(r -> r
                        .path("/v3/api-docs/review")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://REVIEW"))
                .route(r -> r
                        .path("/v3/api-docs/logo")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://LOGO"))
                .route(r -> r
                        .path("/v3/api-docs/mail")
                        .and()
                        .method(HttpMethod.GET)
                        .uri("lb://MAIL"))
                .build();
    }
}
