package com.ryazancev.auth.util.exception.custom;

import com.ryazancev.auth.util.exception.ErrorCode;
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
public class UserNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public UserNotFoundException(final String message,
                                 final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public UserNotFoundException byId(final MessageSource source,
                                      final String id) {

        String message = source.getMessage(
                "exception.auth.not_found_by_id",
                new Object[]{id},
                Locale.getDefault()
        );
        return new UserNotFoundException(
                message,
                ErrorCode.AUTH_SERVICE_NOT_FOUND
        );
    }

    public UserNotFoundException byEmail(final MessageSource source,
                                         final String email) {

        String message = source.getMessage(
                "exception.auth.not_found_by_email",
                new Object[]{email},
                Locale.getDefault()
        );
        return new UserNotFoundException(
                message,
                ErrorCode.AUTH_SERVICE_NOT_FOUND
        );
    }

    public UserNotFoundException byCustomerId(final MessageSource source,
                                              final String customerId) {

        String message = source.getMessage(
                "exception.auth.not_found_by_customer_id",
                new Object[]{customerId},
                Locale.getDefault()
        );
        return new UserNotFoundException(
                message,
                ErrorCode.AUTH_SERVICE_NOT_FOUND
        );
    }
}
