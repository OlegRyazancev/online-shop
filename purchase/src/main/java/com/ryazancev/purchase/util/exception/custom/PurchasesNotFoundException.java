package com.ryazancev.purchase.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PurchasesNotFoundException extends RuntimeException{
    private HttpStatus httpStatus;

    public PurchasesNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
