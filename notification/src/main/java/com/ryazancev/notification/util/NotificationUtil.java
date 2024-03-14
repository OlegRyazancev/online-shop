package com.ryazancev.notification.util;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationStatus;
import com.ryazancev.notification.model.Content;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.model.notification.PrivateNotification;
import com.ryazancev.notification.model.notification.PublicNotification;
import com.ryazancev.notification.service.ContentService;
import com.ryazancev.notification.util.exception.custom.InvalidScopeException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationUtil {

    private final MessageSource messageSource;
    private final ContentService contentService;

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

    public  <T extends Notification> T buildNotification(
            NotificationRequest request,
            Class<T> targetType) {

        Content content = contentService.generateContent(
                request.getType(),
                request.getProperties());

        if (request.getScope() == NotificationScope.PRIVATE) {
            return targetType.cast(PrivateNotification.builder()
                    .senderId(request.getSenderId())
                    .recipientId(request.getRecipientId())
                    .content(content)
                    .timestamp(LocalDateTime.now())
                    .status(NotificationStatus.UNREAD)
                    .build());
        } else {
            return targetType.cast(PublicNotification.builder()
                    .senderId(request.getSenderId())
                    .content(content)
                    .timestamp(LocalDateTime.now())
                    .status(NotificationStatus.UNREAD)
                    .build());
        }
    }
}
