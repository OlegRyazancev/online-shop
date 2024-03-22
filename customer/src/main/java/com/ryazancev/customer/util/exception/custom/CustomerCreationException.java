package com.ryazancev.customer.util.exception.custom;

import com.ryazancev.customer.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class CustomerCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public CustomerCreationException(final String message,
                                     final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public CustomerCreationException emailExists() {

        return new CustomerCreationException(
                CustomErrorCode.OS_CUSTOMER_201_400.getMessage(),
                CustomErrorCode.OS_CUSTOMER_201_400
        );
    }
}
