package com.ryazancev.customer.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class CustomerNotFoundException extends RuntimeException {
    private HttpStatus httpStatus;

    public CustomerNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
