package com.ryazancev.product.util.exception.custom;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.util.exception.CustomErrorCode;
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
                CustomErrorCode.OS_PRODUCT_101_403
                        .getMessage(objectType, objectId),
                CustomErrorCode.OS_PRODUCT_101_403
        );
    }

    public AccessDeniedException emailNotConfirmed() {

        return new AccessDeniedException(
                CustomErrorCode.OS_PRODUCT_103_403.getMessage(),
                CustomErrorCode.OS_PRODUCT_103_403
        );
    }

    public AccessDeniedException accountLocked() {

        return new AccessDeniedException(
                CustomErrorCode.OS_PRODUCT_102_403.getMessage(),
                CustomErrorCode.OS_PRODUCT_102_403
        );
    }

    public AccessDeniedException statusAccess(final ProductStatus status) {

        return new AccessDeniedException(
                CustomErrorCode.OS_PRODUCT_104_403.getMessage(status),
                CustomErrorCode.OS_PRODUCT_104_403
        );
    }
}
