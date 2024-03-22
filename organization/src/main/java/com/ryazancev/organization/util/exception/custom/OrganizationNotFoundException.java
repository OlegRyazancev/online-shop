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
public class OrganizationNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private CustomErrorCode code;
    private LocalDateTime timestamp;

    public OrganizationNotFoundException(final String message,
                                         final CustomErrorCode code) {
        super(message);
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public OrganizationNotFoundException byId(final String id) {

        return new OrganizationNotFoundException(
                CustomErrorCode.OS_ORGANIZATION_301_404.getMessage(id),
                CustomErrorCode.OS_ORGANIZATION_301_404
        );
    }
}
