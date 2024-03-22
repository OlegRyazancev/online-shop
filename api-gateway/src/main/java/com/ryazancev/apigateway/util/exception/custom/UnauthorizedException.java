package com.ryazancev.apigateway.util.exception.custom;

import com.ryazancev.apigateway.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class UnauthorizedException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;


    public UnauthorizedException(final String message,
                                 final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.UNAUTHORIZED;
        this.timestamp = LocalDateTime.now();
        this.code = code;
    }

    public UnauthorizedException unauthorized() {

        return new UnauthorizedException(
                CustomErrorCode.OS_API_GATEWAY_101_401.getMessage(),
                CustomErrorCode.OS_API_GATEWAY_101_401
        );
    }

    public UnauthorizedException unauthorizedRequest() {

        return new UnauthorizedException(
                CustomErrorCode.OS_API_GATEWAY_102_401.getMessage(),
                CustomErrorCode.OS_API_GATEWAY_102_401
        );
    }

    public UnauthorizedException insufficientPrivileges() {

        return new UnauthorizedException(
                CustomErrorCode.OS_API_GATEWAY_103_401.getMessage(),
                CustomErrorCode.OS_API_GATEWAY_103_401
        );
    }
}
