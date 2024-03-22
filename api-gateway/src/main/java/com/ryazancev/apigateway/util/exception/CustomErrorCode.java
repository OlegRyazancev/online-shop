package com.ryazancev.apigateway.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {

    OS_API_GATEWAY_101_401("OS-API_GATEWAY-101-401"),
    OS_API_GATEWAY_102_401("OS-API_GATEWAY-102-401"),
    OS_API_GATEWAY_103_401("OS-API_GATEWAY-103-401"),
    OS_API_GATEWAY_INTERNAL_500("OS-API_GATEWAY-INTERNAL-500");

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
