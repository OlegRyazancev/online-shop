package com.ryazancev.admin.util;

import com.ryazancev.admin.util.exception.CustomExceptionFactory;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class AdminUtil {

    public RequestStatus castStatus(final String status) {

        try {
            return RequestStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {

            throw CustomExceptionFactory
                    .getInvalidRequestStatus()
                    .invalidStatus(status);
        }
    }
}
