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
public class IncorrectBalanceException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public IncorrectBalanceException(final String message,
                                     final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public IncorrectBalanceException insufficientFunds(
            final MessageSource source) {

        String message = source.getMessage(
                "exception.purchase.insufficient_funds",
                null,
                Locale.getDefault()
        );

        return new IncorrectBalanceException(
                message,
                ErrorCode.INSUFFICIENT_FUNDS
        );
    }
}
