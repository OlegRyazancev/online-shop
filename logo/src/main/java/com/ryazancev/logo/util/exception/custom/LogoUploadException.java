package com.ryazancev.logo.util.exception.custom;

import com.ryazancev.logo.util.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class LogoUploadException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public LogoUploadException(final String message,
                               final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public LogoUploadException failed(final MessageSource source,
                                      final String cause) {

        String message = source.getMessage(
                "exception.logo.upload_failed",
                new Object[]{cause},
                Locale.getDefault()
        );
        return new LogoUploadException(
                message,
                ErrorCode.LOGO_UPLOAD_FAILED
        );
    }

    public LogoUploadException mustHaveName(final MessageSource source) {

        String message = source.getMessage(
                "exception.logo.must_have_name",
                null,
                Locale.getDefault()
        );
        return new LogoUploadException(
                message,
                ErrorCode.LOGO_NAME
        );
    }
}
