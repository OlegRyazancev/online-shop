package com.ryazancev.purchase.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ServiceUnavailableException extends RuntimeException {

    private HttpStatus httpStatus;

    public ServiceUnavailableException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
