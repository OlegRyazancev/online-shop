package com.ryazancev.review.kafka;

import com.ryazancev.common.dto.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
public class ReviewProducerService {

    private final KafkaTemplate<String,
            NotificationRequest> notificationKafkaTemplate;

    @Value("${spring.kafka.topic.notification}")
    private String notificationTopic;

    public ReviewProducerService(
            @Qualifier("notificationKafkaTemplate")
            KafkaTemplate<String, NotificationRequest>
                    notificationKafkaTemplate) {

        this.notificationKafkaTemplate = notificationKafkaTemplate;
    }

    public void sendNotification(NotificationRequest request) {

        log.info("Received request to send {} notification {} to user " +
                        "with id: {} from recipient with id: {}",
                request.getScope(),
                request.getType(),
                request.getRecipientId(),
                request.getSenderId());

        try {

            log.trace("Sending notification request...");
            notificationKafkaTemplate.send(notificationTopic, request);

            log.debug("Notification request sent to topic: {}",
                    notificationTopic);

        } catch (Exception e) {

            log.error("Failed to send notification request: {}",
                    e.getMessage());
        }
    }
}
