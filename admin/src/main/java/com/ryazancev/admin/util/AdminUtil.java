package com.ryazancev.admin.util;

import com.ryazancev.admin.util.exception.custom.InvalidRequestStatusException;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
public class AdminUtil {

    public RequestStatus castStatus(String status) {

        try {

            return RequestStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidRequestStatusException(
                    "Invalid request status",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
