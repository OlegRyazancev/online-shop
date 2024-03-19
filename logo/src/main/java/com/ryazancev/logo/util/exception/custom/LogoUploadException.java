package com.ryazancev.logo.util.exception.custom;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class LogoUploadException extends RuntimeException {

    private HttpStatus httpStatus;

    public LogoUploadException(final String message,
                               final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
