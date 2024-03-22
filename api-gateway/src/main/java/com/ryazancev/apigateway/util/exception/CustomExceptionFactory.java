package com.ryazancev.apigateway.util.exception;

import com.ryazancev.apigateway.util.exception.custom.UnauthorizedException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static UnauthorizedException getUnauthorized() {

        return new UnauthorizedException();
    }
}
