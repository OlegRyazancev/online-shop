package com.ryazancev.logo.util.exception.custom;

import com.ryazancev.logo.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class LogoUploadException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public LogoUploadException(final String message,
                               final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public LogoUploadException failed(final String cause) {

        return new LogoUploadException(
                CustomErrorCode.OS_LOGO_101_400.getMessage(cause),
                CustomErrorCode.OS_LOGO_101_400
        );
    }

    public LogoUploadException mustHaveName() {

        return new LogoUploadException(
                CustomErrorCode.OS_LOGO_102_400.getMessage(),
                CustomErrorCode.OS_LOGO_102_400
        );
    }
}
