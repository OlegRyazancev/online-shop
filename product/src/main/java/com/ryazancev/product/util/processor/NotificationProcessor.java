package com.ryazancev.product.util.processor;

import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.RequestHeader;
import jakarta.servlet.http.HttpServletRequest;
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

    public NotificationRequest createNotification(
            ReviewDto reviewDto, Long organizationId) {

        Long senderId = reviewDto
                .getPurchase().safelyCast(PurchaseDto.class, false)
                .getCustomer().safelyCast(CustomerDto.class, false)
                .getId();

        Long recipientId =
                (Long) clientsService
                        .getOwnerByOrganizationId(organizationId);

        Properties properties = new Properties();
        properties.setProperty(
                "product_rating",
                reviewDto.getRating().toString()
        );
        properties.setProperty(
                "product_name",
                reviewDto
                        .getPurchase().safelyCast(PurchaseDto.class, false)
                        .getProduct().safelyCast(ProductDto.class, false)
                        .getProductName()
        );

        return NotificationRequest.builder()
                .scope(NotificationScope.PRIVATE)
                .type(NotificationType.PRIVATE_REVIEW_CREATED)
                .senderId(senderId)
                .recipientId(recipientId)
                .properties(properties)
                .build();
    }
}
