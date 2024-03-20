package com.ryazancev.purchase.util.exception.custom;

import com.ryazancev.purchase.util.exception.ErrorCode;
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
public class PurchaseNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public PurchaseNotFoundException(final String message,
                                     final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public PurchaseNotFoundException byId(final MessageSource source,
                                          final String productId) {

        String message = source.getMessage(
                "exception.purchase.not_found_by_id",
                new Object[]{productId},
                Locale.getDefault()
        );

        return new PurchaseNotFoundException(
                message,
                ErrorCode.PURCHASE_NOT_FOUND_BY_ID
        );
    }
}
