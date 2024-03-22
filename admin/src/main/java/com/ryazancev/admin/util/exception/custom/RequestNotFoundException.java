package com.ryazancev.admin.util.exception.custom;

import com.ryazancev.admin.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class RequestNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public RequestNotFoundException(final String message,
                                    final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public RequestNotFoundException byId(final String id) {

        return new RequestNotFoundException(
                CustomErrorCode.OS_ADMIN_201_404.getMessage(id),
                CustomErrorCode.OS_ADMIN_201_404
        );
    }
}
