package com.ryazancev.auth.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;

    public UserNotFoundException(final String message,
                                 final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
