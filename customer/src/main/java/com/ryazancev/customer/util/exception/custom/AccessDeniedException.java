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

    public AccessDeniedException emailNotConfirmed(final MessageSource source) {

        String message = source.getMessage(
                "exception.customer.email_not_confirmed",
                null,
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_DENIED_EMAIL
        );
    }

    public AccessDeniedException cannotAccessCustomer(
            final MessageSource source,
            final String id) {

        String message = source.getMessage(
                "exception.customer.access_customer",
                new Object[]{id},
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_CUSTOMER
        );
    }

    public AccessDeniedException accountLocked(final MessageSource source) {

        String message = source.getMessage(
                "exception.customer.account_locked",
                null,
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCOUNT_LOCKED
        );
    }

    public AccessDeniedException cannotAccessNotifications(
            final MessageSource source) {

        String message = source.getMessage(
                "exception.customer.access_notifications",
                null,
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_NOTIFICATIONS
        );
    }

    public AccessDeniedException cannotAccessPrivateNotification(
            final MessageSource source,
            final String id) {

        String message = source.getMessage(
                "exception.customer.access_private_notification",
                new Object[]{id},
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_PRIVATE_NOTIFICATION
        );
    }
}
