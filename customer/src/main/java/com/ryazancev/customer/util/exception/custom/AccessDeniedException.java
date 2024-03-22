package com.ryazancev.customer.util.exception.custom;

import com.ryazancev.customer.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class AccessDeniedException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public AccessDeniedException(final String message,
                                 final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public AccessDeniedException emailNotConfirmed() {

        return new AccessDeniedException(
                CustomErrorCode.OS_CUSTOMER_105_403.getMessage(),
                CustomErrorCode.OS_CUSTOMER_105_403
        );
    }

    public AccessDeniedException cannotAccessCustomer(final String id) {

        return new AccessDeniedException(
                CustomErrorCode.OS_CUSTOMER_101_403.getMessage(id),
                CustomErrorCode.OS_CUSTOMER_101_403
        );
    }

    public AccessDeniedException accountLocked() {

        return new AccessDeniedException(
                CustomErrorCode.OS_CUSTOMER_104_403.getMessage(),
                CustomErrorCode.OS_CUSTOMER_104_403
        );
    }

    public AccessDeniedException cannotAccessNotifications() {

        return new AccessDeniedException(
                CustomErrorCode.OS_CUSTOMER_102_403.getMessage(),
                CustomErrorCode.OS_CUSTOMER_102_403
        );
    }

    public AccessDeniedException cannotAccessPrivateNotification(
            final String id) {

        return new AccessDeniedException(
                CustomErrorCode.OS_CUSTOMER_103_403.getMessage(id),
                CustomErrorCode.OS_CUSTOMER_103_403
        );
    }
}
