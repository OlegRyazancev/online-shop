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

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange,
                             final Throwable ex) {

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

    private Mono<Void> handleResponseStatusException(
            final ServerHttpResponse response,
            final ResponseStatusException ex) {

        response.setStatusCode(ex.getStatusCode());

        return response.setComplete();
    }

    private Mono<Void> handleUnauthorizedException(
            final ServerHttpResponse response,
            final UnauthorizedException ex) {

        ExceptionBody exceptionBody = new ExceptionBody(
                ex.getMessage(),
                ex.getHttpStatus(),
                ex.getCode(),
                ex.getTimestamp());

        return writeErrorResponse(response, exceptionBody);
    }

    private Mono<Void> handleDefaultException(
            final ServerHttpResponse response,
            final Throwable ex) {

        ExceptionBody exceptionBody = new ExceptionBody(
                CustomErrorCode.OS_API_GATEWAY_INTERNAL_500
                        .getMessage(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR,
                CustomErrorCode.OS_API_GATEWAY_INTERNAL_500,
                LocalDateTime.now());

        return writeErrorResponse(response, exceptionBody);
    }

    private Mono<Void> writeErrorResponse(
            final ServerHttpResponse response,
            final ExceptionBody exceptionBody) {

        response.setStatusCode(exceptionBody.getHttpStatus());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String responseBody = "{"
                + "\"message\":\""
                + exceptionBody.getMessage()
                + "\","
                + "\"httpStatus\":\""
                + exceptionBody.getHttpStatus().value()
                + "\","
                + "\"errorCode\":\""
                + exceptionBody.getCode().name()
                + "\","
                + "\"timestamp\":\""
                + exceptionBody.getTimestamp().toString()
                + "\""
                + "}";

        return response.writeWith(
                Mono.just(response.bufferFactory()
                        .wrap(responseBody.getBytes())));
    }
}
