package com.ryazancev.notification.util;

import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.util.exception.custom.InvalidScopeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationUtil {

    public NotificationScope castScope(String scope) {

        try {
            return NotificationScope.valueOf(scope.toUpperCase());
        } catch (Exception e) {

            throw new InvalidScopeException(
                    "Invalid scope provided",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
