package com.ryazancev.notification.util.exception;

import com.ryazancev.notification.util.exception.custom.InvalidScopeException;
import com.ryazancev.notification.util.exception.custom.NotificationNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static InvalidScopeException getInvalidScope() {

        return new InvalidScopeException();
    }

    public static NotificationNotFoundException getNotificationNotFound() {

        return new NotificationNotFoundException();
    }
}
