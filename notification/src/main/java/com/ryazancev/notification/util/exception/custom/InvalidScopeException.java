package com.ryazancev.notification.util.exception.custom;

import com.ryazancev.notification.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class InvalidScopeException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public InvalidScopeException(final String message,
                                 final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public InvalidScopeException invalidScope() {

        return new InvalidScopeException(
                CustomErrorCode.OS_NOTIFICATION_201_400.getMessage(),
                CustomErrorCode.OS_NOTIFICATION_201_400
        );
    }
}
