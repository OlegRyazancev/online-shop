package com.ryazancev.notification.service;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.notification.model.notification.Notification;
import com.ryazancev.notification.model.notification.PrivateNotification;
import com.ryazancev.notification.model.notification.PublicNotification;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

public interface NotificationService {

    List<Notification> getNotificationsByRecipientId(
            Long customerId, NotificationScope castedScope);

    Notification getById(String id, NotificationScope castedScope);

    PublicNotification createPublicNotification(NotificationRequest request);

    PrivateNotification createPrivateNotification(NotificationRequest request);

    Long getRecipientIdByPrivateNotificationId(String id);
}
