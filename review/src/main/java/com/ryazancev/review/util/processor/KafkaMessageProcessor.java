package com.ryazancev.review.util.processor;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.review.kafka.ReviewProducerService;
import com.ryazancev.review.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final ReviewProducerService reviewProducerService;
    private final NotificationProcessor notificationProcessor;


    public void sendReviewCreatedNotification(Review review) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor.createNotification(review);

        reviewProducerService.sendNotification(privateNotificationRequest);
    }
}
