package com.ryazancev.notification.util;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationStatus;
import com.ryazancev.notification.model.Content;
import com.ryazancev.notification.model.notification.AdminNotification;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.model.notification.PrivateNotification;
import com.ryazancev.notification.model.notification.PublicNotification;
import com.ryazancev.notification.service.ContentService;
import com.ryazancev.notification.util.exception.CustomExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationUtil {

    private final ContentService contentService;

    public NotificationScope castScope(final String scope) {

        try {

            return NotificationScope.valueOf(scope.toUpperCase());
        } catch (Exception e) {

            throw CustomExceptionFactory
                    .getInvalidScope()
                    .invalidScope();
        }
    }

    public <T extends Notification> T buildNotification(
            final NotificationRequest request,
            final Class<T> targetType) {

        Content content = contentService.generateContent(
                request.getType(),
                request.getProperties());

        switch (request.getScope()) {
            case PRIVATE -> {

                return targetType.cast(PrivateNotification.builder()
                        .senderId(request.getSenderId())
                        .recipientId(request.getRecipientId())
                        .content(content)
                        .timestamp(LocalDateTime.now())
                        .status(NotificationStatus.UNREAD)
                        .build());
            }
            case PUBLIC -> {

                return targetType.cast(PublicNotification.builder()
                        .senderId(request.getSenderId())
                        .content(content)
                        .timestamp(LocalDateTime.now())
                        .build());
            }
            case ADMIN -> {

                return targetType.cast(AdminNotification.builder()
                        .senderId(request.getSenderId())
                        .content(content)
                        .timestamp(LocalDateTime.now())
                        .build());
            }
            default -> {

                return null;
            }
        }
    }
}
