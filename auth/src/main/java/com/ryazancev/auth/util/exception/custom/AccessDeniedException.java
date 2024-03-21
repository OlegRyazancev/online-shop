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
public class AccessDeniedException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public AccessDeniedException(final String message,
                                 final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public AccessDeniedException invalidRefresh(final MessageSource source) {

        String message = source.getMessage(
                "exception.auth.invalid_refresh",
                null,
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.AUTH_SERVICE_INVALID_REFRESH
        );
    }

    public AccessDeniedException deletedAccount(final MessageSource source,
                                                final String email,
                                                final String deletionDate) {

        String message = source.getMessage(
                "exception.auth.deleted_account_format",
                new Object[]{email, deletionDate},
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.AUTH_SERVICE_DELETED_ACCOUNT
        );
    }
}


