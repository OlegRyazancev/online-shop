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
    public void deleteReviewsByProductId(Long productId) {

        log.info("Receive message to delete reviews of product with id: {}",
                productId);

        String result = reviewService.deleteByProductId(productId);

        log.info(result);
    }
}
