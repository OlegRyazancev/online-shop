package com.ryazancev.auth.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class ConfirmationTokenException extends RuntimeException {

    private HttpStatus httpStatus;


    public ConfirmationTokenException(
            String message,
            HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;
    }
}
