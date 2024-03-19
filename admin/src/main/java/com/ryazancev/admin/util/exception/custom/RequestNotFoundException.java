package com.ryazancev.admin.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class RequestNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;

    public RequestNotFoundException(final String message,
                                    final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
