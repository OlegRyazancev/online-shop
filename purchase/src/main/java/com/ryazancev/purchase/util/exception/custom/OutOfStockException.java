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
public class OutOfStockException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public OutOfStockException(final String message,
                               final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public OutOfStockException noProducts(final MessageSource source) {

        String message = source.getMessage(
                "exception.purchase.no_products_in_stock",
                null,
                Locale.getDefault()
        );

        return new OutOfStockException(
                message,
                ErrorCode.NO_PRODUCTS
        );
    }
}
