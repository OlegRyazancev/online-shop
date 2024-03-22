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
public class UserNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public UserNotFoundException(final String message,
                                 final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public UserNotFoundException byId(final String id) {

        return new UserNotFoundException(
                CustomErrorCode.OS_AUTH_401_404.getMessage(id),
                CustomErrorCode.OS_AUTH_401_404
        );
    }

    public UserNotFoundException byEmail(final String email) {

        return new UserNotFoundException(
                CustomErrorCode.OS_AUTH_402_404.getMessage(email),
                CustomErrorCode.OS_AUTH_402_404
        );
    }

    public UserNotFoundException byCustomerId(final String customerId) {

        return new UserNotFoundException(
                CustomErrorCode.OS_AUTH_403_404.getMessage(customerId),
                CustomErrorCode.OS_AUTH_403_404
        );
    }
}
