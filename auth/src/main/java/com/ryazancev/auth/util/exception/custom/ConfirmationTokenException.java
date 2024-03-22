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
public class ConfirmationTokenException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public ConfirmationTokenException(final String message,
                                      final CustomErrorCode code,
                                      final HttpStatus status) {
        super(message);
        this.httpStatus = status;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ConfirmationTokenException emailConfirmed() {

        return new ConfirmationTokenException(
                CustomErrorCode.OS_AUTH_201_400.getMessage(),
                CustomErrorCode.OS_AUTH_201_400,
                HttpStatus.BAD_REQUEST
        );
    }

    public ConfirmationTokenException notFound() {

        return new ConfirmationTokenException(
                CustomErrorCode.OS_AUTH_203_404.getMessage(),
                CustomErrorCode.OS_AUTH_203_404,
                HttpStatus.NOT_FOUND
        );
    }

    public ConfirmationTokenException expired(final String expiredDate) {

        return new ConfirmationTokenException(
                CustomErrorCode.OS_AUTH_202_403.getMessage(expiredDate),
                CustomErrorCode.OS_AUTH_202_403,
                HttpStatus.FORBIDDEN
        );
    }
}
