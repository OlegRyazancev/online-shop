package com.ryazancev.organization.util.exception.custom;

import com.ryazancev.organization.util.exception.ErrorCode;
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
public class OrganizationNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public OrganizationNotFoundException(final String message,
                                         final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public OrganizationNotFoundException byId(final MessageSource source,
                                              final String id) {

        String message = source.getMessage(
                "exception.organization.not_found",
                new Object[]{id},
                Locale.getDefault()
        );
        return new OrganizationNotFoundException(
                message,
                ErrorCode.ORGANIZATION_NOT_FOUND_BY_ID
        );
    }
}
