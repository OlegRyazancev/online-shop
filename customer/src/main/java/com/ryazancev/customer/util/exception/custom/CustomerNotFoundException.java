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
public class CustomerNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public CustomerNotFoundException(final String message,
                                     final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public CustomerNotFoundException byId(final String id) {

        return new CustomerNotFoundException(
                CustomErrorCode.OS_CUSTOMER_301_404.getMessage(id),
                CustomErrorCode.OS_CUSTOMER_301_404
        );
    }
}
