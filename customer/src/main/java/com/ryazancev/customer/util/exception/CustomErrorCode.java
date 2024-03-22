package com.ryazancev.customer.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {
    OS_CUSTOMER_101_403("OS-CUSTOMER-101-403"),
    OS_CUSTOMER_102_403("OS-CUSTOMER-102-403"),
    OS_CUSTOMER_103_403("OS-CUSTOMER-103-403"),
    OS_CUSTOMER_104_403("OS-CUSTOMER-104-403"),
    OS_CUSTOMER_105_403("OS-CUSTOMER-105-403"),
    OS_CUSTOMER_201_400("OS-CUSTOMER-201-400"),
    OS_CUSTOMER_301_404("OS-CUSTOMER-301-404"),
    OS_CUSTOMER_SERVICE_UNAVAILABLE_503("OS-CUSTOMER-SERVICE_UNAVAILABLE-503"),
    OS_CUSTOMER_SECURITY("OS-CUSTOMER-SECURITY"),
    OS_CUSTOMER_INTERNAL_500("OS-CUSTOMER-INTERNAL-500");

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
