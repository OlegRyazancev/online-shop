package com.ryazancev.purchase.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class IncorrectBalanceException extends RuntimeException {

    private HttpStatus httpStatus;


    public IncorrectBalanceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
