package com.ryazancev.notification.kafka;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.notification.model.notification.AdminNotification;
import com.ryazancev.notification.model.notification.PrivateNotification;
import com.ryazancev.notification.model.notification.PublicNotification;
import com.ryazancev.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${spring.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void consumeMessage(NotificationRequest request) {

        log.info("Receive request to send {} notification",
                request.getScope());

        try {
            switch (request.getScope()) {
                case PRIVATE -> {

                    log.trace("Creating private notification...");
                    PrivateNotification notification =
                            notificationService
                                    .createPrivateNotification(request);

                    log.trace("Sending private notification with id {} " +
                                    "by websocket...",
                            notification.getId());
                    messagingTemplate.convertAndSendToUser(
                            String.valueOf(notification.getRecipientId()),
                            "/private",
                            notification);

                    log.debug("Private notification was successfully send");
                }

                case PUBLIC -> {

                    log.trace("Creating public notification...");
                    PublicNotification notification =
                            notificationService
                                    .createPublicNotification(request);

                    log.trace("Sending public notification with id {} " +
                                    "by websocket...",
                            notification.getId());
                    messagingTemplate.convertAndSend(
                            "/public",
                            notification);

                    log.debug("Public notification to users  " +
                            "was successfully send");
                }

                case ADMIN -> {

                    log.trace("Creating admin notification...");
                    AdminNotification notification =
                            notificationService
                                    .createAdminNotification(request);

                    log.trace("Sending admin notification with id {} " +
                                    "by websocket...",
                            notification.getId());
                    messagingTemplate.convertAndSend(
                            "/admin",
                            notification);

                    log.debug("Public notification to users  " +
                            "was successfully send");
                }
                default -> {

                    log.warn("Unknown scope: {}", request.getScope());
                }

            }
        } catch (Exception e) {

            log.error("Notification was not sent: {}", e.getMessage());
        }

    }
}