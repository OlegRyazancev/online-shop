package com.ryazancev.review.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ReviewNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;


    public ReviewNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
