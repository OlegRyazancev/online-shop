package com.ryazancev.product.util.exception.custom;

import com.ryazancev.product.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */


@Getter
@NoArgsConstructor
public class ProductNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public ProductNotFoundException(final String message,
                                    final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ProductNotFoundException byOrganizationId(final String id) {

        return new ProductNotFoundException(
                CustomErrorCode.OS_PRODUCT_302_404.getMessage(id),
                CustomErrorCode.OS_PRODUCT_302_404
        );
    }

    public ProductNotFoundException byId(final String id) {

        return new ProductNotFoundException(
                CustomErrorCode.OS_PRODUCT_301_404.getMessage(id),
                CustomErrorCode.OS_PRODUCT_301_404
        );
    }
}
