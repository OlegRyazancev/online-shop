package com.ryazancev.organization.util.exception.custom;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.organization.model.OrganizationStatus;
import com.ryazancev.organization.util.exception.CustomErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Getter
@NoArgsConstructor
public class AccessDeniedException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public AccessDeniedException(final String message,
                                 final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public AccessDeniedException cannotAccessObject(final ObjectType objectType,
                                                    final String objectId) {

        return new AccessDeniedException(
                CustomErrorCode.OS_ORGANIZATION_101_403.getMessage(
                        objectType, objectId),
                CustomErrorCode.OS_ORGANIZATION_101_403
        );
    }

    public AccessDeniedException statusAccess(final OrganizationStatus status) {

        return new AccessDeniedException(
                CustomErrorCode.OS_ORGANIZATION_104_403.getMessage(status),
                CustomErrorCode.OS_ORGANIZATION_104_403
        );
    }

    public AccessDeniedException emailNotConfirmed() {

        return new AccessDeniedException(
                CustomErrorCode.OS_ORGANIZATION_103_403.getMessage(),
                CustomErrorCode.OS_ORGANIZATION_103_403
        );
    }

    public AccessDeniedException accountLocked() {

        return new AccessDeniedException(
                CustomErrorCode.OS_ORGANIZATION_102_403.getMessage(),
                CustomErrorCode.OS_ORGANIZATION_102_403
        );
    }
}
