package com.ryazancev.apigateway.util.exception;

import com.ryazancev.apigateway.util.exception.custom.UnauthorizedException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();

        if (ex instanceof ResponseStatusException) {
            return handleResponseStatusException(
                    response,
                    (ResponseStatusException) ex);
        }

        if (ex instanceof UnauthorizedException) {
            return handleUnauthorizedException(
                    response,
                    (UnauthorizedException) ex);
        }

        return handleDefaultException(response, ex);
    }

    private Mono<Void> handleResponseStatusException
            (ServerHttpResponse response,
             ResponseStatusException ex) {

        response.setStatusCode(ex.getStatusCode());

        return response.setComplete();
    }

    private Mono<Void> handleUnauthorizedException(
            ServerHttpResponse response,
            UnauthorizedException ex) {

        ExceptionBody exceptionBody = new ExceptionBody(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED);

        return writeErrorResponse(response, exceptionBody);
    }

    private Mono<Void> handleDefaultException(
            ServerHttpResponse response,
            Throwable ex) {

        ExceptionBody exceptionBody = new ExceptionBody(
                "Internal Server Error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);

        return writeErrorResponse(response, exceptionBody);
    }

    private Mono<Void> writeErrorResponse(
            ServerHttpResponse response,
            ExceptionBody exceptionBody) {

        response.setStatusCode(exceptionBody.getHttpStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String responseBody = "{"
                + "\"message\":\"" +
                exceptionBody.getMessage() + "\","

                + "\"httpStatus\":\"" +
                exceptionBody.getHttpStatus().value() + "\""

                + "}";

        return response.writeWith(
                Mono.just(response.bufferFactory()
                        .wrap(responseBody.getBytes())));
    }
}
