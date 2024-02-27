package com.ryazancev.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class ServiceUnavailableException extends RuntimeException {

    private HttpStatus httpStatus;

    public ServiceUnavailableException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
