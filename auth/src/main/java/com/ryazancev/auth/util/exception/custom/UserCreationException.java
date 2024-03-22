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
public class UserCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public UserCreationException(final String message,
                                 final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public UserCreationException emailExists() {

        return new UserCreationException(
                CustomErrorCode.OS_AUTH_301_400.getMessage(),
                CustomErrorCode.OS_AUTH_301_400
        );
    }

    public UserCreationException passwordMismatch() {

        return new UserCreationException(
                CustomErrorCode.OS_AUTH_302_400.getMessage(),
                CustomErrorCode.OS_AUTH_302_400
        );
    }
}
