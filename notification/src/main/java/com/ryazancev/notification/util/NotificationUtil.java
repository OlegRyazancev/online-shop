package com.ryazancev.notification.util;

import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.util.exception.custom.InvalidScopeException;
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
public class NotificationUtil {

    private final MessageSource messageSource;

    public NotificationScope castScope(String scope) {

        try {

            return NotificationScope.valueOf(scope.toUpperCase());
        } catch (Exception e) {

            throw new InvalidScopeException(
                    messageSource.getMessage(
                            "invalid_scope",
                            null, Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
