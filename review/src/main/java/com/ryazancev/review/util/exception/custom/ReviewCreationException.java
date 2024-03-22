package com.ryazancev.review.util.exception.custom;

import com.ryazancev.review.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class ReviewCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public ReviewCreationException(final String message,
                                   final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ReviewCreationException duplicate(final String purchaseId) {

        return new ReviewCreationException(
                CustomErrorCode.OS_REVIEW_101_400.getMessage(purchaseId),
                CustomErrorCode.OS_REVIEW_101_400
        );
    }
}
