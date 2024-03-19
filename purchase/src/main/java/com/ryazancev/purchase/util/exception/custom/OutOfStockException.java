package com.ryazancev.purchase.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class OutOfStockException extends RuntimeException {

    private HttpStatus httpStatus;

    public OutOfStockException(final String message,
                               final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
