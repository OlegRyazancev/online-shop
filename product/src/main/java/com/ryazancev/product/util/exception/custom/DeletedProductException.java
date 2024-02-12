package com.ryazancev.product.util.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class DeletedProductException  extends RuntimeException{

    private HttpStatus httpStatus;

    public DeletedProductException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
