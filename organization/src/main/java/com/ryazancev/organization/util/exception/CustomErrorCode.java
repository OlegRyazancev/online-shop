package com.ryazancev.organization.util.exception;

import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

public enum CustomErrorCode {
    OS_ORGANIZATION_101_403("OS-ORGANIZATION-101-403"),
    OS_ORGANIZATION_102_403("OS-ORGANIZATION-102-403"),
    OS_ORGANIZATION_103_403("OS-ORGANIZATION-103-403"),
    OS_ORGANIZATION_104_403("OS-ORGANIZATION-104-403"),
    OS_ORGANIZATION_201_400("OS-ORGANIZATION-201-400"),
    OS_ORGANIZATION_202_400("OS-ORGANIZATION-202-400"),
    OS_ORGANIZATION_301_404("OS-ORGANIZATION-301-404"),
    OS_ORGANIZATION_SERVICE_UNAVAILABLE_503
            ("OS-ORGANIZATION-SERVICE_UNAVAILABLE-503"),
    OS_ORGANIZATION_SECURITY("OS-ORGANIZATION-SECURITY"),
    OS_ORGANIZATION_INTERNAL_500("OS-ORGANIZATION-INTERNAL-500");

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
