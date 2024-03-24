package com.ryazancev.product.util.processor;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.util.RequestHeader;
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

    public NotificationRequest createAdminNotification(
            final ReviewDto reviewDto,
            final Long organizationId,
            final RequestHeader requestHeader) {

        Long senderId = requestHeader.getUserId();

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

    public NotificationRequest createAdminNotification(
            final RequestHeader requestHeader) {

        Properties properties = new Properties();
        properties.setProperty("object_type", ObjectType.PRODUCT.name());

        return NotificationRequest.builder()
                .type(NotificationType.ADMIN_NEW_REGISTRATION_REQUEST_RECEIVED)
                .scope(NotificationScope.ADMIN)
                .properties(properties)
                .senderId(requestHeader.getUserId())
                .build();
    }
}
