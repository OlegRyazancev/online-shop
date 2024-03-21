package com.ryazancev.admin.util.exception.custom;

import com.ryazancev.admin.util.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class InvalidRequestStatusException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public InvalidRequestStatusException(final String message,
                                         final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public InvalidRequestStatusException invalidStatus(
            final MessageSource source,
            final String status) {

        String message = source.getMessage(
                "exception.admin.invalid_status",
                new Object[]{status},
                Locale.getDefault()
        );
        return new InvalidRequestStatusException(
                message,
                ErrorCode.INVALID_STATUS
        );
    }
}
