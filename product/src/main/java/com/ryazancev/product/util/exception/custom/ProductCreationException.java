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
public class ProductCreationException extends RuntimeException {


    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public ProductCreationException(final String message,
                                    final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public ProductCreationException nameExists() {

        return new ProductCreationException(
                CustomErrorCode.OS_PRODUCT_201_400.getMessage(),
                CustomErrorCode.OS_PRODUCT_201_400
        );
    }
}
