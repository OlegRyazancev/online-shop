package com.ryazancev.apigateway.filter;

import com.ryazancev.apigateway.util.JwtUtil;
import com.ryazancev.apigateway.util.exception.CustomExceptionFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Oleg Ryazancev
 */

@Component
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(final RouteValidator validator,
                                final JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(final Config config) {

        return (((exchange, chain) -> {
            ServerHttpRequest request = null;

            if (validator.getIsSecured().test(exchange.getRequest())) {

                if (!exchange.getRequest()
                        .getHeaders()
                        .containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw CustomExceptionFactory
                            .getUnauthorized()
                            .unauthorized();
                }
                String token = Objects.requireNonNull(
                        exchange.getRequest()
                                .getHeaders()
                                .get(HttpHeaders.AUTHORIZATION)).get(0);

                if (token != null
                        && token.startsWith("Bearer ")) {

                    token = token.substring(7);
                }

                if (!jwtUtil.validateToken(token)) {

                    throw CustomExceptionFactory
                            .getUnauthorized()
                            .unauthorizedRequest();
                }
                List<String> roles = jwtUtil.extractRoles(token);

                if (exchange.getRequest().getURI().getPath()
                        .contains("/admin")) {

                    if (!roles.contains("ROLE_ADMIN")) {

                        throw CustomExceptionFactory
                                .getUnauthorized()
                                .insufficientPrivileges();
                    }
                }
                String rolesString = String.join(" ", roles);

                request = exchange.getRequest()
                        .mutate()
                        .header("userId",
                                jwtUtil.extractId(token))
                        .header("email",
                                jwtUtil.extractEmail(token))
                        .header("roles",
                                rolesString)
                        .header("locked",
                                jwtUtil.extractLocked(token))
                        .header("confirmed",
                                jwtUtil.extractConfirmed(token))
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
