package com.ryazancev.product.util.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@AllArgsConstructor
@Getter
@Setter
public class ProductCreationException extends RuntimeException {

    private HttpStatus httpStatus;

    public ProductCreationException(final String message,
                                    final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
