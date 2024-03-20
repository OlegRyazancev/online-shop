package com.ryazancev.notification.util.exception.custom;

import com.ryazancev.notification.util.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class InvalidScopeException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public InvalidScopeException(final String message,
                                 final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public InvalidScopeException invalidScope(final MessageSource source) {

        String message = source.getMessage(
                "exception.notification.invalid_scope",
                null,
                Locale.getDefault()
        );
        return new InvalidScopeException(
                message,
                ErrorCode.INVALID_SCOPE
        );
    }
}
