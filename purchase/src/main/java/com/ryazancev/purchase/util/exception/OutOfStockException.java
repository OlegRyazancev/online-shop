package com.ryazancev.purchase.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class OutOfStockException extends RuntimeException {

    private HttpStatus httpStatus;

    public OutOfStockException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
