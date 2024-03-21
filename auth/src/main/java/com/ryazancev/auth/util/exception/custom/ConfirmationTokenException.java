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
public class ConfirmationTokenException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public ConfirmationTokenException(final String message,
                                      final ErrorCode code,
                                      final HttpStatus status) {
        super(message);
        this.httpStatus = status;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ConfirmationTokenException emailConfirmed(
            final MessageSource source) {

        String message = source.getMessage(
                "exception.auth.email_confirmed",
                null,
                Locale.getDefault()
        );
        return new ConfirmationTokenException(
                message,
                ErrorCode.AUTH_SERVICE_EMAIl_CONFIRMED,
                HttpStatus.BAD_REQUEST
        );
    }

    public ConfirmationTokenException notFound(final MessageSource source){

        String message = source.getMessage(
                "exception.auth.token_not_found",
                null,
                Locale.getDefault()
        );
        return new ConfirmationTokenException(message,
                ErrorCode.AUTH_SERVICE_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }

    public ConfirmationTokenException expired(final MessageSource source,
                                              final String expiredDate) {

        String message = source.getMessage(
                "exception.auth.token_expired",
                new Object[]{expiredDate},
                Locale.getDefault()
        );
        return new ConfirmationTokenException(
                message,
                ErrorCode.AUTH_SERVICE_TOKEN_EXPIRED,
                HttpStatus.FORBIDDEN
        );
    }
}
