package com.ryazancev.notification.util.exception.custom;

import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class NotificationNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;


    public NotificationNotFoundException(final String message,
                                         final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public NotificationNotFoundException byId(final NotificationScope scope,
                                              final String id) {

        return new NotificationNotFoundException(
                CustomErrorCode.OS_NOTIFICATION_101_404.getMessage(scope, id),
                CustomErrorCode.OS_NOTIFICATION_101_404
        );
    }
}
