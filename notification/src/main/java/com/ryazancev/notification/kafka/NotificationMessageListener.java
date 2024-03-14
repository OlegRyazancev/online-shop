package com.ryazancev.notification.kafka;

import com.ryazancev.common.dto.notification.NotificationRequest;
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


        switch (request.getScope()) {
            case PRIVATE -> {

                log.info("Receive message to send private notification" +
                                " to user with id: {}",
                        request.getRecipientId());

                log.trace("Creating private notification...");
                PrivateNotification notification =
                        notificationService.createPrivateNotification(request);

                log.trace("Sending private notification with id {} " +
                                "by websocket...",
                        notification.getId());
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(notification.getRecipientId()),
                        "/private",
                        notification);

                log.info("Private notification was successfully send");
            }

            case PUBLIC -> {

                log.info("Receive message to send public notification " +
                        "to all users");

                log.trace("Creating public notification...");
                PublicNotification notification =
                        notificationService.createPublicNotification(request);

                log.trace("Sending public notification with id {}",
                        notification.getId());
                messagingTemplate.convertAndSend(
                        "/public",
                        notification);

                log.info("Public notification to users  " +
                        "was successfully send");
            }
            default -> {

                log.warn("Unknown scope: {}", request.getScope());
            }

        }
    }
}