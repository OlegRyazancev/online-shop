package com.ryazancev.purchase.util.processor;

import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.notification.enums.NotificationScope;
import com.ryazancev.common.dto.notification.enums.NotificationType;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.service.ClientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class NotificationProcessor {

    private final ClientsService clientsService;

    public NotificationRequest createNotification(Purchase purchase) {

        Properties properties = new Properties();

        String productName = clientsService
                .getSimpleProductById(purchase.getProductId())
                .safelyCast(ProductDto.class, false)
                .getProductName();

        properties.setProperty("product_name", productName);

        Long recipientId = (Long) clientsService
                .getProductOwnerId(purchase.getProductId());

        return NotificationRequest.builder()
                .scope(NotificationScope.PRIVATE)
                .type(NotificationType.PRIVATE_PURCHASE_PROCESSED)
                .properties(properties)
                .senderId(purchase.getCustomerId())
                .recipientId(recipientId)
                .build();

    }
}
