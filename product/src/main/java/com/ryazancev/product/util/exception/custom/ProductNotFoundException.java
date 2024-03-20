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
public class ProductNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public ProductNotFoundException(final String message,
                                    final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ProductNotFoundException byOrganizationId(final MessageSource source,
                                                     final String id) {

        String message = source.getMessage(
                "exception.product.not_found_by_organization_id",
                new Object[]{id},
                Locale.getDefault()
        );
        return new ProductNotFoundException(
                message,
                ErrorCode.PRODUCT_NOT_FOUND_BY_ID
        );
    }

    public ProductNotFoundException byId(final MessageSource source,
                                         final String id) {

        String message = source.getMessage(
                "exception.product.not_found_by_id",
                new Object[]{id},
                Locale.getDefault()
        );

        return new ProductNotFoundException(
                message,
                ErrorCode.PRODUCT_NOT_FOUND_BY_ORGANIZATION_ID
        );
    }
}
