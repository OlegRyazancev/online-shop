package com.ryazancev.notification.util.exception.custom;

import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.util.exception.ErrorCode;
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
public class NotificationNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;


    public NotificationNotFoundException(final String message,
                                         final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public NotificationNotFoundException byId(final MessageSource source,
                                              final NotificationScope scope,
                                              final String id) {

        String message = source.getMessage(
                "exception.notification.not_found_by_id",
                new Object[]{scope, id},
                Locale.getDefault()
        );

        return new NotificationNotFoundException(
                message,
                ErrorCode.NOTIFICATION_NOT_FOUND_BY_ID
        );
    }
}
