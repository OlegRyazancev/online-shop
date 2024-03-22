package com.ryazancev.admin.util.exception.custom;

import com.ryazancev.admin.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class InvalidRequestStatusException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public InvalidRequestStatusException(final String message,
                                         final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public InvalidRequestStatusException invalidStatus(final String status) {

        return new InvalidRequestStatusException(
                CustomErrorCode.OS_ADMIN_101_400.getMessage(status),
                CustomErrorCode.OS_ADMIN_101_400
        );
    }
}
