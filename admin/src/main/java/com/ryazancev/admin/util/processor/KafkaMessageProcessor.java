package com.ryazancev.admin.util.processor;

import com.ryazancev.admin.kafka.AdminProducerService;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final NotificationProcessor notificationProcessor;
    private final AdminProducerService adminProducerService;

    public void sendRegisterResponse(final RegistrationRequest request) {

        adminProducerService.sendRegisterResponse(request);
    }

    public void sendPrivateNotification(final RegistrationRequest request) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendPublicNotification(final RegistrationRequest request) {

        NotificationRequest publicNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PUBLIC
                        );

        adminProducerService.sendNotification(publicNotificationRequest);
    }

    public void sendPrivateNotification(final ObjectRequest request) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendPrivateNotification(final UserLockRequest request) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendMessageToChangeObjectStatus(final ObjectRequest request) {

        adminProducerService.sendMessageToChangeObjectStatus(request);
    }

    public void sendMessageToToggleUserLock(final UserLockRequest request) {

        adminProducerService.sendMessageToToggleUserLock(request);
    }
}
