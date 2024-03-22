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
public class IncorrectBalanceException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public IncorrectBalanceException(final String message,
                                     final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public IncorrectBalanceException insufficientFunds() {

        return new IncorrectBalanceException(
                CustomErrorCode.OS_PURCHASE_201_400.getMessage(),
                CustomErrorCode.OS_PURCHASE_201_400
        );
    }
}
