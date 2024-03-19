package com.ryazancev.apigateway.filter;

import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Oleg Ryazancev
 */

@Component
@Getter
public class RouteValidator {

    public static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/eureka"
    );

    private final Predicate<ServerHttpRequest> isSecured =
            request -> OPEN_API_ENDPOINTS
                    .stream()
                    .noneMatch(uri -> request
                            .getURI()
                            .getPath()
                            .contains(uri));
}

