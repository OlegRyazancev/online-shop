package com.ryazancev.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@Setter
@NoArgsConstructor
public class ServiceUnavailableException extends RuntimeException {

    private HttpStatus httpStatus;
    private String code;
    private LocalDateTime timestamp;
    private String errorCode;

    public ServiceUnavailableException(final String message,
                                       final String errorCode) {
        super(message);
        this.code = "SERVICE_UNAVAILABLE";
        this.httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
    }

    public ServiceUnavailableException get(final String message,
                                           final String errorCode) {

        return new ServiceUnavailableException(message, errorCode);
    }
}
