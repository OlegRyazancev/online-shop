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
public class OrganizationCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public OrganizationCreationException(final String message,
                                         final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public OrganizationCreationException nameExists(
            final MessageSource source) {

        String message = source.getMessage(
                "exception.organization.name_exists",
                null,
                Locale.getDefault()
        );
        return new OrganizationCreationException(
                message,
                ErrorCode.ORGANIZATION_CREATION_NAME
        );
    }

    public OrganizationCreationException descriptionExists(
            final MessageSource source) {

        String message = source.getMessage(
                "exception.organization.description_exists",
                null,
                Locale.getDefault()
        );
        return new OrganizationCreationException(
                message,
                ErrorCode.ORGANIZATION_CREATION_DESCRIPTION
        );
    }
}
