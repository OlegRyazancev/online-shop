package com.ryazancev.review.repository;

import com.ryazancev.review.model.Review;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class DataSetup {


    private final ReviewRepository reviewRepository;
    private static final long[] CUSTOMER_IDS = {1L, 2L, 3L, 4L, 5L};
    private static final long[] PRODUCT_IDS = {1L, 2L, 3L, 4L, 5L};
    private static final String[] PURCHASES_IDS = new String[13];
    private static int reviewIdCounter = 1;

    static {
        for (int i = 0; i < PURCHASES_IDS.length; i++) {
            PURCHASES_IDS[i] = String.valueOf(i + 1);
        }
    }


    @PostConstruct
    public void setup() {

        reviewRepository.deleteAll();

        createReview(PURCHASES_IDS[0], 5);
        createReview(PURCHASES_IDS[1], 4);
        createReview(PURCHASES_IDS[2], 3);
        createReview(PURCHASES_IDS[3], 2);
        createReview(PURCHASES_IDS[4], 1);
        createReview(PURCHASES_IDS[5], 2);
        createReview(PURCHASES_IDS[6], 3);
        createReview(PURCHASES_IDS[7], 4);
        createReview(PURCHASES_IDS[8], 5);
        createReview(PURCHASES_IDS[9], 1);
        createReview(PURCHASES_IDS[10], 1);
        createReview(PURCHASES_IDS[11], 2);
    }

    private void createReview(final String purchaseId,
                              final int totalSaves) {

        LocalDateTime createdAt =
                LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);

        for (int i = 0; i < totalSaves; i++) {

            reviewRepository.save(Review.builder()
                    .id(String.valueOf(reviewIdCounter++))
                    .productId(PRODUCT_IDS[i])
                    .customerId(CUSTOMER_IDS[i])
                    .body("Example body")
                    .rating(i + 1)
                    .purchaseId(purchaseId)
                    .createdAt(createdAt.plusMonths(i))
                    .build());
        }
    }
}
