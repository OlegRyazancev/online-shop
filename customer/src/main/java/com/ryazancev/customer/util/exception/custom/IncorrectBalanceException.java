package com.ryazancev.customer.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class IncorrectBalanceException extends RuntimeException {

    private HttpStatus httpStatus;

    public IncorrectBalanceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
