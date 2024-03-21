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
public class RequestNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public RequestNotFoundException(final String message,
                                    final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public RequestNotFoundException byId(final MessageSource source,
                                         final String id) {

        String message = source.getMessage(
                "exception.admin.not_found_by_id",
                new Object[]{id},
                Locale.getDefault()
        );
        return new RequestNotFoundException(
                message,
                ErrorCode.REQUEST_NOT_FOUND_BY_ID
        );
    }
}
