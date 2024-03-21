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
public class CustomerNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public CustomerNotFoundException(final String message,
                                     final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public CustomerNotFoundException byId(final MessageSource source,
                                          final String id) {

        String message = source.getMessage(
                "exception.customer.not_found_by_id",
                new Object[]{id},
                Locale.getDefault()
        );
        return new CustomerNotFoundException(
                message,
                ErrorCode.NOT_FOUND
        );
    }
}
