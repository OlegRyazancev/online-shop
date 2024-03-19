package com.ryazancev.admin.util.processor;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.notification.NotificationAttributeDeterminer;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationAttributeDeterminer determiner;

    public NotificationRequest createNotification(
            final RegistrationRequest request,
            final NotificationScope scope) {

        NotificationType type =
                determiner.determineNotificationType(
                        request,
                        scope
                );

        Properties properties =
                determiner.determineProperties(
                        request.getObjectType(),
                        request.getObjectToRegisterId(),
                        type
                );
        Long recipientId =
                determiner.determineRecipientId(
                        request.getObjectType(),
                        request.getObjectToRegisterId(),
                        scope
                );
        Long senderId =
                determiner.determineSenderId(
                        scope,
                        recipientId
                );

        return NotificationRequest.builder()
                .scope(scope)
                .type(type)
                .properties(properties)
                .senderId(senderId)
                .recipientId(
                        scope == NotificationScope.ADMIN
                                ? null
                                : recipientId
                )
                .build();
    }


    public NotificationRequest createNotification(
            final ObjectRequest request,
            final NotificationScope scope) {

        NotificationType type = determiner.determineNotificationType(request);

        Long recipientId =
                determiner.determineRecipientId(
                        request.getObjectType(),
                        request.getObjectId(),
                        scope
                );

        Long senderId =
                determiner.determineSenderId();

        Properties properties =
                determiner.determineProperties(
                        request.getObjectType(),
                        request.getObjectId(),
                        type
                );

        return NotificationRequest.builder()
                .scope(scope)
                .type(type)
                .properties(properties)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

    }

    public NotificationRequest createNotification(
            final UserLockRequest request,
            final NotificationScope scope) {

        NotificationType type = determiner.determineNotificationType(request);

        Long recipientId = request.getUserId();

        Long senderId =
                determiner.determineSenderId();

        return NotificationRequest.builder()
                .scope(scope)
                .type(type)
                .recipientId(recipientId)
                .senderId(senderId)
                .properties(new Properties())
                .build();
    }
}
