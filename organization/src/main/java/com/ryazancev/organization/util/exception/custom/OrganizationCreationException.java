package com.ryazancev.organization.util.exception.custom;

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
public class OrganizationCreationException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public OrganizationCreationException(final String message,
                                         final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public OrganizationCreationException nameExists() {

        return new OrganizationCreationException(
                CustomErrorCode.OS_ORGANIZATION_202_400.getMessage(),
                CustomErrorCode.OS_ORGANIZATION_202_400
        );
    }

    public OrganizationCreationException descriptionExists() {

        return new OrganizationCreationException(
                CustomErrorCode.OS_ORGANIZATION_201_400.getMessage(),
                CustomErrorCode.OS_ORGANIZATION_201_400
        );
    }
}
