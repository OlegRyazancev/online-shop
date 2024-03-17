package com.ryazancev.review.util.processor;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.review.model.Review;
import com.ryazancev.review.service.ClientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final ClientsService clientsService;

    public NotificationRequest createNotification(Review review,
                                                  NotificationScope scope) {


        Properties properties = new Properties();

        String productName = clientsService
                .getSimpleProductById(review.getProductId())
                .safelyCast(ProductDto.class, false)
                .getProductName();

        properties.setProperty(
                "product_rating",
                review.getRating().toString()
        );
        properties.setProperty(
                "product_name",
                productName
        );

        Long recipientId =
                clientsService.getOwnerIdByProductId(review.getProductId());

        return NotificationRequest.builder()
                .scope(scope)
                .type(NotificationType.PRIVATE_REVIEW_CREATED)
                .properties(properties)
                .senderId(review.getCustomerId())
                .recipientId(recipientId)
                .build();


    }
}
