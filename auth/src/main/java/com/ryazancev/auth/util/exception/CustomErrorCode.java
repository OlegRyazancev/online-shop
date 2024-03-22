package com.ryazancev.auth.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {
    OS_AUTH_101_403("OS-AUTH-101-403"),
    OS_AUTH_102_403("OS-AUTH-102-403"),
    OS_AUTH_201_400("OS-AUTH-201-400"),
    OS_AUTH_202_403("OS-AUTH-202-403"),
    OS_AUTH_203_404("OS-AUTH-203-404"),
    OS_AUTH_301_400("OS-AUTH-301-400"),
    OS_AUTH_302_400("OS-AUTH-302-400"),
    OS_AUTH_401_404("OS-AUTH-401-404"),
    OS_AUTH_402_404("OS-AUTH-402-404"),
    OS_AUTH_403_404("OS-AUTH-403-404"),
    OS_AUTH_SERVICE_UNAVAILABLE_503("OS-AUTH-SERVICE_UNAVAILABLE-503"),
    OS_AUTH_SECURITY("OS-AUTH-SECURITY"),
    OS_AUTH_INTERNAL_500("OS-AUTH-INTERNAL-500");

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
