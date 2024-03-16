package com.ryazancev.admin.util;

import com.ryazancev.admin.util.exception.custom.InvalidRequestStatusException;
import com.ryazancev.common.dto.admin.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class AdminUtil {

    private final MessageSource messageSource;

    public RequestStatus castStatus(String status) {

        try {
            return RequestStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidRequestStatusException(
                    messageSource.getMessage(
                            "exception.admin.invalid_status",
                            new Object[]{status},
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
