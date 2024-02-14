package com.ryazancev.customer.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AccessDeniedException extends RuntimeException {

    HttpStatus httpStatus;

    public AccessDeniedException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
