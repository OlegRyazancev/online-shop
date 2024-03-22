package com.ryazancev.purchase.util.exception.custom;

import com.ryazancev.purchase.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class OutOfStockException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public OutOfStockException(final String message,
                               final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.CONFLICT;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public OutOfStockException noProducts() {

        return new OutOfStockException(
                CustomErrorCode.OS_PURCHASE_301_409.getMessage(),
                CustomErrorCode.OS_PURCHASE_301_409
        );
    }
}
