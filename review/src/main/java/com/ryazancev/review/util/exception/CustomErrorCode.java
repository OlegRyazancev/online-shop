package com.ryazancev.review.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {
    OS_REVIEW_101_400("OS-REVIEW-101-400"),
    OS_REVIEW_INTERNAL_500("OS-REVIEW-INTERNAL-500"),
    OS_REVIEW_SECURITY("OS-REVIEW-SECURITY"),
    OS_REVIEW_SERVICE_UNAVAILABLE_503("OS-REVIEW-SERVICE_UNAVAILABLE-503");

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
