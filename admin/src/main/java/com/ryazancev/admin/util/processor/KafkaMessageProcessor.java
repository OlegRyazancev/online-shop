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

    public void sendAdminNotification(RegistrationRequest request) {

        NotificationRequest notificationRequest =
                notificationProcessor.createNotification(
                        request,
                        NotificationScope.ADMIN);

        adminProducerService.sendNotification(notificationRequest);
    }

    public void sendRegisterResponse(RegistrationRequest request) {

        adminProducerService.sendRegisterResponse(request);
    }

    public void sendPrivateNotification(RegistrationRequest request) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendPublicNotification(RegistrationRequest request) {

        NotificationRequest publicNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PUBLIC
                        );

        adminProducerService.sendNotification(publicNotificationRequest);
    }

    public void sendPrivateNotification(ObjectRequest request) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendPrivateNotification(UserLockRequest request) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendMessageToChangeObjectStatus(ObjectRequest request) {

        adminProducerService.sendMessageToChangeObjectStatus(request);
    }

    public void sendMessageToToggleUserLock(UserLockRequest request) {

        adminProducerService.sendMessageToToggleUserLock(request);
    }
}
