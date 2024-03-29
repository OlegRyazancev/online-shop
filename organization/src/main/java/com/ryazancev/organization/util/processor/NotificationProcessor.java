package com.ryazancev.organization.util.processor;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.organization.util.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    public NotificationRequest createAdminNotification(
            final RequestHeader requestHeader) {

        Properties properties = new Properties();
        properties.setProperty("object_type", ObjectType.ORGANIZATION.name());

        return NotificationRequest.builder()
                .type(NotificationType.ADMIN_NEW_REGISTRATION_REQUEST_RECEIVED)
                .scope(NotificationScope.ADMIN)
                .properties(properties)
                .senderId(requestHeader.getUserId())
                .build();
    }
}
