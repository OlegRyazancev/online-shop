package com.ryazancev.apigateway.filter;

import com.ryazancev.apigateway.util.JwtUtil;
import com.ryazancev.apigateway.util.exception.custom.UnauthorizedException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest()
                        .getHeaders()
                        .containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException("Unauthorized");
                }
                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .get(HttpHeaders.AUTHORIZATION)
                        .get(0);

                if (authHeader != null
                        && authHeader.startsWith("Bearer ")) {

                    authHeader = authHeader.substring(7);
                }

                if (!jwtUtil.validateToken(authHeader)) {
                    throw new UnauthorizedException("Unauthorized request");
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}
