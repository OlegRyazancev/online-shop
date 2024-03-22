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
public class PurchaseNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public PurchaseNotFoundException(final String message,
                                     final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public PurchaseNotFoundException byId(final String id) {

        return new PurchaseNotFoundException(
                CustomErrorCode.OS_PURCHASE_101_404.getMessage(id),
                CustomErrorCode.OS_PURCHASE_101_404
        );
    }
}
