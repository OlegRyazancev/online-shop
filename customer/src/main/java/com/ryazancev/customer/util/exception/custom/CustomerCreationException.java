package com.ryazancev.customer.util.exception.custom;

import com.ryazancev.customer.util.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class CustomerCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public CustomerCreationException(final String message,
                                     final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public CustomerCreationException emailExists(final MessageSource source) {

        String message = source.getMessage(
                "exception.customer.customer_email_exists",
                null,
                Locale.getDefault()
        );
        return new CustomerCreationException(
                message,
                ErrorCode.EMAIL_EXISTS
        );
    }
}
