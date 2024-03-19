package com.ryazancev.review.kafka;

import com.ryazancev.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewMessageListeners {

    private final ReviewService reviewService;

    @KafkaListener(
            topics = "${spring.kafka.topic.review}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    public void deleteReviewsByProductId(final Long productId) {

        log.info("Received message to delete reviews for product with ID: {}",
                productId);
        try {

            log.debug("Deleting reviews...");
            String deletionResult = reviewService.deleteByProductId(productId);

            log.debug(deletionResult);

        } catch (Exception e) {

            log.error("Failed to delete reviews for product with ID {}: {}",
                    productId, e.getMessage());
        }
    }
}
