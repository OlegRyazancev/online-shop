package com.ryazancev.admin.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {
    OS_ADMIN_201_404("OS-ADMIN-201-404"),
    OS_ADMIN_101_400("OS-ADMIN-101-400"),
    OS_ADMIN_SECURITY("OS-ADMIN-SECURITY"),
    OS_ADMIN_SERVICE_UNAVAILABLE_503("OS-ADMIN-SERVICE_UNAVAILABLE-503"),
    OS_ADMIN_INTERNAL_500("OS-ADMIN-INTERNAL-500");

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
