package com.ryazancev.review.util.exception.custom;

import com.ryazancev.review.util.exception.ErrorCode;
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
public class ReviewCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public ReviewCreationException(final String message,
                                   final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ReviewCreationException duplicate(final MessageSource source,
                                             final String purchaseId) {

        String message = source.getMessage(
                "exception.review.duplicate_review",
                new Object[]{purchaseId},
                Locale.getDefault()
        );

        return new ReviewCreationException(message, ErrorCode.DUPLICATE);
    }
}
