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
public class UserCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public UserCreationException(final String message,
                                 final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public UserCreationException emailExists(final MessageSource source) {

        String message = source.getMessage(
                "exception.auth.email_exists",
                null,
                Locale.getDefault()
        );
        return new UserCreationException(
                message,
                ErrorCode.AUTH_SERVICE_EMAIL_EXISTS
        );
    }

    public UserCreationException passwordMismatch(final MessageSource source) {

        String message = source.getMessage(
                "exception.auth.password_mismatch",
                null,
                Locale.getDefault()
        );
        return new UserCreationException(
                message,
                ErrorCode.AUTH_SERVICE_PASSWORD_MISMATCH
        );
    }
}
