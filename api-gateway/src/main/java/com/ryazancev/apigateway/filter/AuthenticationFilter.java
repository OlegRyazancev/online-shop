package com.ryazancev.apigateway.filter;

import com.ryazancev.apigateway.util.JwtUtil;
import com.ryazancev.apigateway.util.exception.custom.UnauthorizedException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;


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
            ServerHttpRequest request = null;
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest()
                        .getHeaders()
                        .containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new UnauthorizedException("Unauthorized");
                }
                String token = exchange.getRequest()
                        .getHeaders()
                        .get(HttpHeaders.AUTHORIZATION)
                        .get(0);

                if (token != null
                        && token.startsWith("Bearer ")) {

                    token = token.substring(7);
                }

                if (!jwtUtil.validateToken(token)) {
                    throw new UnauthorizedException("Unauthorized request");
                }
                request = exchange.getRequest()
                        .mutate()
                        .header("loggedUsername",
                                jwtUtil.extractUsername(token))
                        .build();
            }
            return chain.filter(exchange
                    .mutate()
                    .request(Objects.requireNonNull(request))
                    .build()
            );
        }));
    }

    public static class Config {

    }
}
