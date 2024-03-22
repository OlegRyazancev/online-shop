package com.ryazancev.auth.util.exception.custom;

import com.ryazancev.auth.util.exception.CustomErrorCode;
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

    public AccessDeniedException invalidRefresh() {

        return new AccessDeniedException(
                CustomErrorCode.OS_AUTH_102_403.getMessage(),
                CustomErrorCode.OS_AUTH_102_403
        );
    }

    public AccessDeniedException deletedAccount(final String email,
                                                final String deletionDate) {

        return new AccessDeniedException(
                CustomErrorCode.OS_AUTH_101_403.getMessage(email, deletionDate),
                CustomErrorCode.OS_AUTH_101_403
        );
    }
}


