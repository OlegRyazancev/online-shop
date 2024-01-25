package com.ryazancev.review.util.exception;

import org.springframework.http.HttpStatus;

public class ReviewCreationException extends RuntimeException{

    private HttpStatus httpStatus;


    public ReviewCreationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
