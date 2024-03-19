package com.ryazancev.notification.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class InvalidScopeException extends RuntimeException {

    private HttpStatus httpStatus;

    public InvalidScopeException(final String message,
                                 final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
