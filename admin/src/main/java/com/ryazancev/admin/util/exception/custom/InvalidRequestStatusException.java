package com.ryazancev.admin.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class InvalidRequestStatusException extends RuntimeException {

    private HttpStatus httpStatus;

    public InvalidRequestStatusException(
            String message,
            HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;
    }
}
