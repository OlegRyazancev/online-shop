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
                                " to user: {}",
                        request.getRecipientId());

                PrivateNotification notification =
                        notificationService.createPrivateNotification(request);

                messagingTemplate.convertAndSendToUser(
                        String.valueOf(notification.getRecipientId()),
                        "/private",
                        notification);

                log.info("Private notification to user with id: {} was " +
                                "successfully send. Notification id: {}",
                        notification.getId(), notification.getRecipientId());
            }

            case PUBLIC -> {

                log.info("Receive message to send public notification " +
                                "to user: {}",
                        request.getRecipientId());

                PublicNotification notification =
                        notificationService.createPublicNotification(request);

                messagingTemplate.convertAndSend(
                        "/public",
                        notification);

                log.info("Public notification to user was successfully " +
                                "send. Notification id: {}",
                        notification.getId());
            }
            default -> {
            }

        }
    }
}