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
    void consumeMessage(final NotificationRequest request) {

        log.info("Received request to send {} notification to user: {}"
                        + " from: {}",
                request.getScope(),
                request.getRecipientId(),
                request.getSenderId());

        try {
            switch (request.getScope()) {
                case PRIVATE -> {

                    log.debug("Creating private notification...");
                    PrivateNotification notification =
                            notificationService
                                    .createPrivateNotification(request);

                    log.debug("Sending private notification with id {} "
                                    + "by websocket...",
                            notification.getId());
                    messagingTemplate.convertAndSendToUser(
                            String.valueOf(notification.getRecipientId()),
                            "/private",
                            notification);

                    log.debug("Private notification with id {} "
                                    + "was successfully sent",
                            notification.getId());
                }

                case PUBLIC -> {

                    log.debug("Creating public notification...");
                    PublicNotification notification =
                            notificationService
                                    .createPublicNotification(request);

                    log.debug("Sending public notification with id {} "
                                    + "by websocket...",
                            notification.getId());
                    messagingTemplate.convertAndSend(
                            "/public",
                            notification);

                    log.debug("Public notification with id {} was "
                                    + "successfully sent",
                            notification.getId());

                }

                case ADMIN -> {

                    log.debug("Creating admin notification...");
                    AdminNotification notification =
                            notificationService
                                    .createAdminNotification(request);

                    log.debug("Sending admin notification with id {} "
                                    + "by websocket...",
                            notification.getId());
                    messagingTemplate.convertAndSend(
                            "/admin",
                            notification);

                    log.debug("Admin notification with id {} was "
                                    + "successfully sent",
                            notification.getId());
                }

                default -> {

                    log.warn("Unknown scope: {}", request.getScope());
                }

            }
        } catch (Exception e) {

            log.error("Failed to send notification: {}", e.getMessage());
        }
    }
}
