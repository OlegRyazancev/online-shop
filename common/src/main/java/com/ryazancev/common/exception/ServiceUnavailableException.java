package com.ryazancev.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
public class ServiceUnavailableException extends RuntimeException {

    private HttpStatus httpStatus;
    private String code;
    private LocalDateTime timestamp;

    public ServiceUnavailableException(final String message,
                                       final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = "SERVICE_UNAVAILABLE";
        this.timestamp = LocalDateTime.now();
    }
}
