package com.ryazancev.review.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class ReviewNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;

    public ReviewNotFoundException(final String message,
                                   final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
