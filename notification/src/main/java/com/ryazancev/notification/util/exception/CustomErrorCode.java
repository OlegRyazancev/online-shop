package com.ryazancev.notification.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {
    OS_NOTIFICATION_101_404("OS-NOTIFICATION-101-404"),
    OS_NOTIFICATION_201_400("OS-NOTIFICATION-201-400"),
    OS_NOTIFICATION_SERVICE_UNAVAILABLE_503
            ("OS-NOTIFICATION-SERVICE_UNAVAILABLE-503"),
    OS_NOTIFICATION_INTERNAL_500("OS-NOTIFICATION-INTERNAL-500");

    @Setter
    private static MessageSource messageSource;
    private final String messageCode;

    CustomErrorCode(final String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage(final Object... args) {
        return messageSource.getMessage(messageCode, args, Locale.getDefault());
    }
}
