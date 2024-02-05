package com.ryazancev.admin.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RequestNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;

    public RequestNotFoundException(
            String message,
            HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;
    }
}
