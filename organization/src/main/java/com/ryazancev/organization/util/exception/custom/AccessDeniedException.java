package com.ryazancev.organization.util.exception.custom;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.util.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class AccessDeniedException extends RuntimeException {

    private HttpStatus httpStatus;
    private ErrorCode code;
    private LocalDateTime timestamp;

    public AccessDeniedException(final String message,
                                 final ErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public AccessDeniedException cannotAccessObject(final MessageSource source,
                                                    final ObjectType objectType,
                                                    final String objectId) {

        String message = source.getMessage(
                "exception.organization.access_object",
                new Object[]{
                        objectType,
                        objectId
                },
                Locale.getDefault()
        );

        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_DENIED_OBJECT
        );
    }

    public AccessDeniedException statusAccess(final MessageSource source,
                                              final OrganizationStatus status) {

        String message =  source.getMessage(
                "exception.organization.status_access",
                new Object[]{status},
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_DENIED_STATUS
        );
    }

    public AccessDeniedException emailNotConfirmed(final MessageSource source) {

        String message = source.getMessage(
                "exception.organization.email_not_confirmed",
                null,
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_DENIED_EMAIL
        );
    }

    public AccessDeniedException accountLocked(final MessageSource source) {

        String message = source.getMessage(
                "exception.organization.account_locked",
                null,
                Locale.getDefault()
        );
        return new AccessDeniedException(
                message,
                ErrorCode.ACCESS_DENIED_ACCOUNT
        );
    }
}
