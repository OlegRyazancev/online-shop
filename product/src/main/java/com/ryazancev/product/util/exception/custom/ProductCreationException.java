package com.ryazancev.product.util.exception.custom;

import com.ryazancev.product.util.exception.ErrorCode;
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
public class ProductCreationException extends RuntimeException {


    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public ProductCreationException(final String message,
                                    final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ProductCreationException nameExists(final MessageSource source) {

        String message = source.getMessage(
                "exception.product.name_exists",
                null,
                Locale.getDefault()
        );

        return new ProductCreationException(
                message,
                ErrorCode.PRODUCT_CREATION_NAME
        );
    }
}
