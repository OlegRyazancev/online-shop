package com.ryazancev.apigateway.util.exception.custom;

/**
 * @author Oleg Ryazancev
 */

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(final String message) {
        super(message);
    }
}
