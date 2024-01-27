package com.ryazancev.logo.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class LogoUploadException extends RuntimeException {
    private HttpStatus httpStatus;

    public LogoUploadException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
