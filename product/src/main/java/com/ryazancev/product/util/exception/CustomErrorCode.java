package com.ryazancev.product.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */


public enum CustomErrorCode {

    OS_PRODUCT_101_403("OS-PRODUCT-101-403"),
    OS_PRODUCT_102_403("OS-PRODUCT-102-403"),
    OS_PRODUCT_103_403("OS-PRODUCT-103-403"),
    OS_PRODUCT_104_403("OS-PRODUCT-104-403"),
    OS_PRODUCT_201_400("OS-PRODUCT-201-400"),
    OS_PRODUCT_301_404("OS-PRODUCT-301-404"),
    OS_PRODUCT_302_404("OS-PRODUCT-302-404"),
    OS_PRODUCT_SECURITY("OS-PRODUCT-SECURITY"),
    OS_PRODUCT_INTERNAL_500("OS-PRODUCT-INTERNAL-500"),
    OS_PRODUCT_SERVICE_UNAVAILABLE_503("OS-PRODUCT-SERVICE_UNAVAILABLE-503");

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
