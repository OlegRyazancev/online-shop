package com.ryazancev.admin.util.notification;

import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.service.ClientsService;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import jakarta.servlet.http.HttpServletRequest;
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

    public NotificationRequest createNotification(RegistrationRequest request,
                                                  NotificationScope scope) {

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

        Long senderId =
                determiner.determineSenderId();

        Long recipientId =
                determiner.determineRecipientId(
                        request.getObjectType(),
                        request.getObjectToRegisterId(),
                        scope
                );

        return NotificationRequest.builder()
                .scope(scope)
                .type(type)
                .properties(properties)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
    }


    public NotificationRequest createNotification(ObjectRequest request,
                                                  NotificationScope scope) {

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
}
