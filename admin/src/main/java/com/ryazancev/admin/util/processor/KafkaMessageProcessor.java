package com.ryazancev.admin.util.processor;

import com.ryazancev.admin.kafka.AdminProducerService;
import com.ryazancev.admin.model.RegistrationRequest;
import com.ryazancev.admin.util.RequestHeader;
import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.UserLockRequest;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProcessor {

    private final NotificationProcessor notificationProcessor;
    private final AdminProducerService adminProducerService;

    @Async("asyncTaskExecutor")
    public void sendRegisterResponse(final RegistrationRequest request) {

        log.info("Sending register response at thread: "
                + Thread.currentThread().getName());

        adminProducerService.sendRegisterResponse(request);
    }

    @Async("asyncTaskExecutor")
    public void sendPrivateNotification(final RegistrationRequest request,
                                        final RequestHeader requestHeader) {

        log.info("Method sendPrivateNotification (RegistrationRequest)"
                + " starts work at thread: "
                + Thread.currentThread().getName());

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE,
                                requestHeader
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    @Async("asyncTaskExecutor")
    public void sendPublicNotification(final RegistrationRequest request,
                                       final RequestHeader requestHeader) {

        log.info("Method sendPublicNotification (RegistrationRequest)"
                + " starts work at thread: "
                + Thread.currentThread().getName());

        NotificationRequest publicNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PUBLIC,
                                requestHeader);

        adminProducerService.sendNotification(publicNotificationRequest);
    }

    @Async("asyncTaskExecutor")
    public void sendPrivateNotification(final ObjectRequest request,
                                        final RequestHeader requestHeader) {

        log.info("Method sendPrivateNotification (ObjectRequest) starts work"
                + " at thread: " + Thread.currentThread().getName());

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE,
                                requestHeader
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    @Async("asyncTaskExecutor")
    public void sendPrivateNotification(final UserLockRequest request,
                                        final RequestHeader requestHeader) {

        log.info("Method sendPrivateNotification (UserLockRequest) starts work"
                + " at thread: " + Thread.currentThread().getName());

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createNotification(
                                request,
                                NotificationScope.PRIVATE,
                                requestHeader
                        );

        adminProducerService.sendNotification(privateNotificationRequest);
    }

    @Async("asyncTaskExecutor")
    public void sendMessageToChangeObjectStatus(final ObjectRequest request) {

        log.info("Method sendMessageToChangeObject starts work at thread: "
                + Thread.currentThread().getName());

        adminProducerService.sendMessageToChangeObjectStatus(request);
    }

    @Async("asyncTaskExecutor")
    public void sendMessageToToggleUserLock(final UserLockRequest request) {

        log.info("Method sendMessageToToggleUserLock starts work at thread: "
                + Thread.currentThread().getName());

        adminProducerService.sendMessageToToggleUserLock(request);
    }
}
