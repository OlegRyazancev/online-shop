package com.ryazancev.purchase.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {

    OS_PURCHASE_101_404("OS-PURCHASE-101-404"),
    OS_PURCHASE_201_400("OS-PURCHASE-201-400"),
    OS_PURCHASE_301_409("OS-PURCHASE-301-409"),
    OS_PURCHASE_SECURITY("OS-PURCHASE-SECURITY"),
    OS_PURCHASE_INTERNAL_500("OS-PURCHASE-INTERNAL-500"),
    OS_PURCHASE_SERVICE_UNAVAILABLE_503("OS-PURCHASE-SERVICE_UNAVAILABLE-503");

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
