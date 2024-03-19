package com.ryazancev.customer.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class CustomerCreationException extends RuntimeException {

    private HttpStatus httpStatus;

    public CustomerCreationException(final String message,
                                     final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
